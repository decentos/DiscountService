package me.decentos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.decentos.entity.model.DiscountItem;
import me.decentos.entity.model.DiscountPercent;
import me.decentos.entity.model.DiscountPeriod;
import me.decentos.entity.model.DiscountUser;
import me.decentos.repository.DiscountItemRepository;
import me.decentos.repository.DiscountUserRepository;
import me.decentos.service.DiscountServiceAsync;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountServiceAsyncImpl implements DiscountServiceAsync {

    private final DiscountItemRepository discountItemRepository;
    private final DiscountUserRepository discountUserRepository;

    @SneakyThrows
    @Async("processExecutor")
    @Override
    public CompletableFuture<String> getDiscountForItem(long itemId, double basePrice, long userId) {
        Thread.sleep(10_000);

        String resultDiscountMessage;
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
                resultDiscountMessage = String.format("Product price on sale: %.2f. The discount was: %d%%", salePrice, salePercent);
            } else {
                resultDiscountMessage = String.format("The final price of the product: %.2f. The discount was: %d%%. Personal discount: %d%%", discountPrice, discountPercent, discountUser.getDiscountConfigure().getPercent());
            }
        } else {
            discountPercent = itemDiscountPercent.getPercent();
            discountPrice = calculateDiscountPrice(basePrice, discountPercent);
            resultDiscountMessage = String.format("Promotional price for the product: %.2f. The discount was: %d%%", discountPrice, discountPercent);
        }
        return CompletableFuture.completedFuture(resultDiscountMessage);
    }

    private double calculateDiscountPrice(double basePrice, int percent) {
        return basePrice - (basePrice / 100 * percent);
    }
}
