package me.decentos.service;

import java.util.concurrent.CompletableFuture;

public interface DiscountServiceAsync {
    CompletableFuture<String> getDiscountForItem(long itemId, double basePrice, long userId);
}
