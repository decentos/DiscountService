package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.decentos.entity.model.DiscountConfigure;
import me.decentos.entity.model.DiscountItem;
import me.decentos.entity.model.DiscountPercent;
import me.decentos.entity.model.DiscountPeriod;
import me.decentos.entity.model.DiscountUser;
import me.decentos.repository.DiscountConfigureRepository;
import me.decentos.repository.DiscountItemRepository;
import me.decentos.repository.DiscountUserRepository;
import me.decentos.service.DiscountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountUserRepository discountUserRepository;
    private final DiscountItemRepository discountItemRepository;
    private final DiscountConfigureRepository discountConfigureRepository;

    @Override
    public String getDiscountForItem(long itemId, double basePrice, long userId) {
        double salePrice = -1;
        int salePercent = -1;
        double discountPrice;
        int discountPercent;
        DiscountItem discountItem = discountItemRepository.findDiscountItemById(itemId);
        DiscountPercent itemDiscountPercent = discountItem.getDiscountPercent();

        if (userId != 0) {
            DiscountPeriod discountPeriod = discountItem.getDiscountPeriod();
            if (LocalDate.now().isAfter(discountPeriod.getStartDate()) && LocalDate.now().isBefore(discountPeriod.getEndDate())) {
                salePercent = discountPeriod.getPercent();
                salePrice = calculateDiscountPrice(basePrice, salePercent);
            }
            discountPercent = itemDiscountPercent.getPercent();
            discountPrice = calculateDiscountPrice(basePrice, discountPercent);

            DiscountUser discountUser = discountUserRepository.findDiscountUserByUserId(userId);
            discountPrice = calculateDiscountPrice(discountPrice, discountUser.getDiscountConfigure().getPercent());

            if (salePrice > 0 && salePercent > 0 && salePrice < discountPrice) {
                return String.format("Product price on sale: %.2f. The discount was: %d%%", salePrice, salePercent);
            } else {
                return String.format("The final price of the product: %.2f. The discount was: %d%%. Personal discount: %d%%", discountPrice, discountPercent, discountUser.getDiscountConfigure().getPercent());
            }
        } else {
            discountPercent = itemDiscountPercent.getPercent();
            discountPrice = calculateDiscountPrice(basePrice, discountPercent);
            return String.format("Promotional price for the product: %.2f. The discount was: %d%%", discountPrice, discountPercent);
        }
    }

    @Override
    public String getPersonalDiscount(long userId) {
        DiscountUser discountUser = discountUserRepository.findDiscountUserByUserId(userId);
        return String.format("Current user discount: %d%%", discountUser.getDiscountConfigure().getPercent());
    }

    @Override
    public String changePersonalDiscount(long userId, int purchaseAmount) {
        List<DiscountConfigure> discountConfigureList = discountConfigureRepository.findAll();
        DiscountUser discountUser = discountUserRepository.findById(userId).orElse(null);

        if (discountUser == null) {
            DiscountConfigure discountConfigure = getDiscountConfigure(purchaseAmount, discountConfigureList);
            discountUserRepository.save(new DiscountUser(userId, purchaseAmount, discountConfigure));
            return String.format("Updated personal discount: %d%%", discountConfigure.getPercent());
        } else {
            purchaseAmount += discountUser.getPurchaseAmount();
            DiscountConfigure discountConfigure = getDiscountConfigure(purchaseAmount, discountConfigureList);
            discountUser.setPurchaseAmount(purchaseAmount);
            discountUser.setDiscountConfigure(discountConfigure);
            discountUserRepository.save(discountUser);
            return String.format("Updated personal discount: %d%%", discountConfigure.getPercent());
        }
    }

    private DiscountConfigure getDiscountConfigure(int purchaseAmount, List<DiscountConfigure> discountConfigureList) {
        return discountConfigureList
                .stream()
                .filter(it -> it.getSumStart() < purchaseAmount && it.getSumEnd() > purchaseAmount)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private double calculateDiscountPrice(double basePrice, int percent) {
        return basePrice - (basePrice / 100 * percent);
    }
}
