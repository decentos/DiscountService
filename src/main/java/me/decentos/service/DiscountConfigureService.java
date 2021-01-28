package me.decentos.service;

import me.decentos.entity.enums.Action;

public interface DiscountConfigureService {
    String discountConfigure(double sum, Action action, int percent);
}
