package com.ksem.oil.domain.dto;

import com.ksem.oil.domain.entity.GasolineType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GasTotalDto {
    String azs;
    LocalDateTime period;
    Double quantity;
    GasolineType gasolineType;
    int tank;
}
