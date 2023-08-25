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
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "GasSales")
@Table(name = "gas_sales")
@Getter
@Setter
public class GasSales {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs")
    Azs azs;

    @ManyToOne
    @JoinColumn(name = "customer")
    Customer customer;

    @Column(name = "t_date")
    LocalDateTime date;

    @Column(name = "g_type")
    GasolineType gasolineType;

    @Column(name = "tank")
    int tank;

    @Column(name = "trk")
    int trk;

    @Column(name = "sales_type")
    Long sales_type;

    @Column(name = "global_sales_type")
    Long globalSalesType;

    @Column(name = "shift")
    Long shift;

    @Column(name = "liter")
    Double liter;

    @Column(name = "count")
    Double count;

    @Column(name = "price")
    Double price;

    @Column(name = "sum")
    Double sum;

    @Column(name = "extid")
    UUID extId;

    @Column(name = "check_number")
    Long checkNumber;

    @Column(name = "manager")
    String manager;

    @Column(name = "comment")
    String comment;

    @Column(name = "row_number")
    int rowNumber;

    @Column(name = "unique_id_order")
    UUID globalId;
}
