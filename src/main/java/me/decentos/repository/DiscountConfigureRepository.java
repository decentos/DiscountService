package me.decentos.repository;

import me.decentos.entity.model.DiscountConfigure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountConfigureRepository extends JpaRepository<DiscountConfigure, Long> {

    DiscountConfigure findDiscountConfigureBySumStart(double sumStart);
}
