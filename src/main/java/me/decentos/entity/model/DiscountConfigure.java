package me.decentos.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount_configure")
public class DiscountConfigure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sum_start")
    private double sumStart;

    @Column(name = "sum_end")
    private double sumEnd;

    @Column(name = "percent")
    private int percent;

    @Column(name = "delete")
    private int isDelete;

    public DiscountConfigure(double sumStart, double sumEnd, int percent) {
        this.sumStart = sumStart;
        this.sumEnd = sumEnd;
        this.percent = percent;
    }
}
