package com.ksem.oil.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerDto {
    String name;
    String azs;
    UUID globalId;
    String edrpou;
    Boolean cashless;
}
