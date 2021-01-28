package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.decentos.entity.enums.Action;
import me.decentos.entity.model.DiscountConfigure;
import me.decentos.repository.DiscountConfigureRepository;
import me.decentos.service.DiscountConfigureService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountConfigureServiceImpl implements DiscountConfigureService {

    private final DiscountConfigureRepository discountConfigureRepository;

    @Override
    public String discountConfigure(double sum, Action action, int percent) {
        List<DiscountConfigure> discountConfigureList = discountConfigureRepository.findAll();
        long matchesCount = discountConfigureList
                .stream()
                .filter(it -> it.getSumStart() == sum || it.getSumEnd() == sum)
                .count();

        if (action == Action.ADD && percent != 0) {
            return addNewConfigure(sum, percent, discountConfigureList, matchesCount);
        } else if (action == Action.EDIT) {
            return editConfigure(sum, discountConfigureList, matchesCount);
        } else if (action == Action.DELETE) {
            return deleteConfigure(sum, discountConfigureList, matchesCount);
        }
        return null;
    }

    private String addNewConfigure(double sum, int percent, List<DiscountConfigure> discountConfigureList, long matchesCount) {
        if (matchesCount == 0) {
            double sumEnd = discountConfigureList
                    .stream()
                    .filter(it -> it.getSumEnd() < sum)
                    .max(Comparator.comparing(DiscountConfigure::getSumEnd))
                    .orElse(new DiscountConfigure())
                    .getSumEnd();

            DiscountConfigure oldConfigure = discountConfigureRepository.findDiscountConfigureBySumStart(sumEnd + 1);
            if (oldConfigure != null) {
                deleteOldConfigure(sum, oldConfigure);
            }
            discountConfigureRepository.save(new DiscountConfigure(sumEnd + 1, sum, percent));

            return printDiscountConfigures();
        } else {
            return String.format("For the amount of a %.2f discount is already configured!", sum);
        }
    }

    private String editConfigure(double sum, List<DiscountConfigure> discountConfigureList, long matchesCount) {
        if (matchesCount > 0) {
            DiscountConfigure discountConfigure = getMatchesConfigure(sum, discountConfigureList);
            if (discountConfigure.getSumStart() == sum) discountConfigure.setSumStart(sum + 1);
            if (discountConfigure.getSumEnd() == sum) discountConfigure.setSumEnd(sum + 1);
            discountConfigureRepository.save(discountConfigure);
            return printDiscountConfigures();
        } else {
            return String.format("For the amount of a %.2f the discount is not configured yet!", sum);
        }
    }

    private String deleteConfigure(double sum, List<DiscountConfigure> discountConfigureList, long matchesCount) {
        if (matchesCount > 0) {
            DiscountConfigure discountConfigure = getMatchesConfigure(sum, discountConfigureList);
            discountConfigure.setIsDelete(1);
            discountConfigureRepository.save(discountConfigure);
            return printDiscountConfigures();
        } else {
            return String.format("For the amount of a %.2f the discount is not configured yet!", sum);
        }
    }

    private void deleteOldConfigure(double sum, DiscountConfigure oldConfigure) {
        oldConfigure.setIsDelete(1);
        discountConfigureRepository.save(oldConfigure);
        discountConfigureRepository.save(new DiscountConfigure(sum + 1, oldConfigure.getSumEnd(), oldConfigure.getPercent()));
    }

    private String printDiscountConfigures() {
        StringBuffer sb = new StringBuffer();
        discountConfigureRepository.findAll()
                .stream()
                .filter(it -> it.getIsDelete() == 0)
                .sorted(Comparator.comparing(DiscountConfigure::getSumStart))
                .forEach(
                        it -> sb.append("From ")
                                .append(String.format("%.2f", it.getSumStart()))
                                .append("$ to ")
                                .append(String.format("%.2f", it.getSumEnd()))
                                .append("$ discount - ")
                                .append(it.getPercent())
                                .append("%\n")
                );
        return sb.toString();
    }

    private DiscountConfigure getMatchesConfigure(double sum, List<DiscountConfigure> discountConfigureList) {
        return discountConfigureList
                .stream()
                .filter(it -> it.getSumStart() == sum || it.getSumEnd() == sum)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
