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

@Entity(name = "Message")
@Table(name = "message")
@Getter
@Setter
public class Message {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "azs")
    private Azs azs;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @Column(name = "t_date")
    LocalDateTime date;

}
