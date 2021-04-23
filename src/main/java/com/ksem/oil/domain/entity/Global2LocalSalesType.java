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

@Entity(name = "Global2LocalSalesType")
@Table(name = "global_local_sales_types")
@Getter
@Setter
public class Global2LocalSalesType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs")
    Azs azs;

    @Column(name = "local_type")
    Long localType;

    @Column(name = "global_type")
    Long globalType;

}
