package me.decentos.repository;

import me.decentos.entity.model.DiscountPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountPeriodRepository extends JpaRepository<DiscountPeriod, Long> {
}
