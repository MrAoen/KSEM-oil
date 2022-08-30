package com.ksem.oil.domain.dto;

import com.ksem.oil.domain.entity.GasolineType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CuponSalesDto implements Serializable {
    String sign;
    LocalDateTime signDate;
    GasolineType gasolineType;
    Long value;
    UUID customerId;
    UUID extId;
    Integer number;
    String description;
}
