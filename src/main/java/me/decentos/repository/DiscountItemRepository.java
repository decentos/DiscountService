package me.decentos.repository;

import me.decentos.entity.model.DiscountItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountItemRepository extends JpaRepository<DiscountItem, Long> {

    DiscountItem findDiscountItemById(long id);
}
