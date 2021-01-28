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
@Table(name = "discount_item")
public class DiscountItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "price")
    private double price;

    @Column(name = "base_price")
    private double basePrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_percent_id")
    private DiscountPercent discountPercent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_period_id")
    private DiscountPeriod discountPeriod;
}
