package me.decentos.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount_user")
public class DiscountUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "purchase_amount")
    private double purchaseAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_configure_id")
    private DiscountConfigure discountConfigure;

    public DiscountUser(long userId, int purchaseAmount, DiscountConfigure discountConfigure) {
        this.userId = userId;
        this.purchaseAmount = purchaseAmount;
        this.discountConfigure = discountConfigure;
    }
}
