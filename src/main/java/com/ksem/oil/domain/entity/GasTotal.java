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

@Entity(name = "GasTotal")
@Table(name = "gas_total")
@Getter
@Setter
public class GasTotal {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs_id")
    Azs azs;

    @Column(name = "period")
    LocalDateTime period;

    @Column(name = "qty")
    Double quantity;

    @Column(name = "g_type")
    GasolineType gasolineType;

    @Column(name = "tank")
    int tank;
}
