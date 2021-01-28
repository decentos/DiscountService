package me.decentos.service;

public interface DiscountService {
    String getDiscountForItem(long itemId, double basePrice, long userId);

    String getPersonalDiscount(long userId);

    String changePersonalDiscount(long userId, int purchaseAmount);
}
