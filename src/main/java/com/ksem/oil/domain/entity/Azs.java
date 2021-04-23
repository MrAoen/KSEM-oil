package com.ksem.oil.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "AZS")
@Table(name = "azs")
@Getter
@Setter
public class Azs {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "name")
    String name;

    @Column(name = "extid")
    UUID extId;

    @JsonIgnore
    @OneToMany(mappedBy = "azs", fetch = FetchType.LAZY)
    List<GasSales> salesList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "azs", fetch = FetchType.LAZY)
    List<Customer> customerList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "azs", fetch = FetchType.LAZY)
    List<MoneyTransaction> moneyTransactionList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "azs", fetch = FetchType.LAZY)
    List<Global2LocalSalesType> global2LocalSalesTypeListList = new ArrayList<>();
}
