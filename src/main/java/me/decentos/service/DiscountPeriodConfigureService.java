package me.decentos.service;

import me.decentos.entity.enums.Action;

import java.time.LocalDate;

public interface DiscountPeriodConfigureService {
    String discountPeriodConfigure(LocalDate startDate, LocalDate endDate, Action action, int percent);
}
