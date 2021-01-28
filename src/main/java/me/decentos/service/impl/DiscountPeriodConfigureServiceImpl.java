package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.decentos.entity.enums.Action;
import me.decentos.entity.model.DiscountPeriod;
import me.decentos.repository.DiscountPeriodRepository;
import me.decentos.service.DiscountPeriodConfigureService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountPeriodConfigureServiceImpl implements DiscountPeriodConfigureService {
    private final DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final DiscountPeriodRepository discountPeriodRepository;

    @Override
    public String discountPeriodConfigure(LocalDate startDate, LocalDate endDate, Action action, int percent) {
        List<DiscountPeriod> discountPeriodList = discountPeriodRepository.findAll();
        long matchesPeriod = getDiscountPeriodStream(startDate, endDate, discountPeriodList).count();

        switch (action) {
            case ADD:
                return addDiscountPeriod(startDate, endDate, percent, matchesPeriod);
            case EDIT:
                return editDiscountPeriod(startDate, endDate, matchesPeriod, discountPeriodList);
            case DELETE:
                return deleteDiscountPeriod(startDate, endDate, matchesPeriod, discountPeriodList);
        }
        return null;
    }

    private String addDiscountPeriod(LocalDate startDate, LocalDate endDate, int percent, long matchesPeriod) {
        if (matchesPeriod == 0) {
            discountPeriodRepository.save(new DiscountPeriod(startDate, endDate, percent));
            return printDiscountPeriodForNextThreeMonths();
        } else {
            return String.format("For the period from %s to %s, the sale is already scheduled!", dTF.format(startDate), dTF.format(endDate));
        }
    }

    private String editDiscountPeriod(LocalDate startDate, LocalDate endDate, long matchesPeriod, List<DiscountPeriod> discountPeriodList) {
        if (matchesPeriod > 0) {
            DiscountPeriod discountPeriod = getDiscountPeriodStream(startDate, endDate, discountPeriodList).findFirst().orElseThrow(NoSuchElementException::new);
            discountPeriod.setStartDate(discountPeriod.getStartDate().plusDays(1));
            discountPeriod.setEndDate(discountPeriod.getEndDate().plusDays(1));
            discountPeriodRepository.save(discountPeriod);
            return printDiscountPeriodForNextThreeMonths();
        } else {
            return String.format("For the period from %s to %s, the sale has not yet been scheduled!", dTF.format(startDate), dTF.format(endDate));
        }
    }

    private String deleteDiscountPeriod(LocalDate startDate, LocalDate endDate, long matchesPeriod, List<DiscountPeriod> discountPeriodList) {
        if (matchesPeriod > 0) {
            DiscountPeriod discountPeriod = getDiscountPeriodStream(startDate, endDate, discountPeriodList).findFirst().orElseThrow(NoSuchElementException::new);
            discountPeriodRepository.delete(discountPeriod);
            return printDiscountPeriodForNextThreeMonths();
        } else {
            return String.format("For the period from %s to %s, the sale has not yet been scheduled!", dTF.format(startDate), dTF.format(endDate));
        }
    }

    private String printDiscountPeriodForNextThreeMonths() {
        StringBuffer sb = new StringBuffer("Upcoming sales:\n");
        discountPeriodRepository.findAll()
                .stream()
                .filter(it -> it.getStartDate().isBefore(LocalDate.now().plusMonths(3)) && LocalDate.now().isBefore(it.getEndDate()))
                .forEach(
                        it -> sb.append("from ")
                                .append(dTF.format(it.getStartDate()))
                                .append(" to ")
                                .append(dTF.format(it.getEndDate()))
                                .append(" discount - ")
                                .append(it.getPercent())
                                .append("%\n")
                );
        return sb.toString();
    }


    private Stream<DiscountPeriod> getDiscountPeriodStream(LocalDate startDate, LocalDate endDate, List<DiscountPeriod> discountPeriodList) {
        return discountPeriodList
                .stream()
                .filter(it -> it.getStartDate().isEqual(startDate) && it.getEndDate().isEqual(endDate));
    }
}
