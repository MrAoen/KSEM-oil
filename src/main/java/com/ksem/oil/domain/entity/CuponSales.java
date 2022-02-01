package com.ksem.oil.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Cupone")
@Table(name = "cupone_sales")
@Getter
@Setter
public class CuponSales {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "sign")
    String sign;

    @Column(name = "sign_date")
    LocalDateTime signDate;

    @Column(name = "gas_type")
    GasolineType gasolineType;

    @Column(name = "value")
    Long value;

    @Column(name = "customer_id")
    UUID customerId;

    @Column(name = "ext_id")
    UUID extId;

    @Column(name = "line_number")
    Integer number;

    @Column(name = "description")
    String description;

}
