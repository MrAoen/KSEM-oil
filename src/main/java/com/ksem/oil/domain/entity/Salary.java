package com.ksem.oil.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Salary")
@Table(name = "salary")
@Getter
@Setter
public class Salary {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs")
    Azs azs;

    @Column(name = "extid")
    UUID extId;

    @Column(name ="t_date")
    LocalDateTime date;

    @Column(name = "employee")
    String employee;

    @Column(name = "position")
    String position;

    @Column(name = "liter")
    Double liter;

    @Column(name = "rate")
    Double rate;

    @Column(name = "sum_liter_rate")
    Double sumLiterRate;

    @Column(name = "premium")
    Double premium;

    @Column(name = "total_salaries")
    Double totalSalaries;

    @Column(name = "prepayment_cashless")
    Double prepaymentCashless;

    @Column(name = "prepayment_cash")
    Double prepaymentCash;

    @Column(name = "pay_out")
    Double payOut;

    @Column(name = "comment")
    String comment;
}
