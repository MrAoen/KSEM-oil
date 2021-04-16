package com.ksem.oil.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity(name = "MoneyTransactions")
@Table(name = "money_transactions")
@Getter
@Setter
public class MoneyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs")
    Azs azs;

    @ManyToOne
    @JoinColumn(name = "customer")
    Customer customer;

    @Column(name = "shift")
    Long shift;

    @Column(name = "sales_type")
    Long sales_type;

    @Column(name = "global_sales_type")
    Long globalSalesType;

    @Column(name = "cost_item")
    Long costItem;

    @Column(name = "sum")
    Double sum;

    @Column(name = "extid")
    UUID extId;

}
