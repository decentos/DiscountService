package me.decentos.repository;

import me.decentos.entity.model.DiscountUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountUserRepository extends JpaRepository<DiscountUser, Long> {

    DiscountUser findDiscountUserByUserId(long userId);
}
