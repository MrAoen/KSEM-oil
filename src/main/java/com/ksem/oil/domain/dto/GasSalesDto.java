package com.ksem.oil.domain.dto;

import com.ksem.oil.domain.entity.GasolineType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GasSalesDto {
    String azs; //основной склад - наименование (или ГУИД, или струткуру строкой)
    GasolineType gasolineType;
    int tank;
    int trk;
    Long sales_type;
    Long shift;
    Double liter;
    Double count;
    Double price;
    Double sum;
    UUID extId;
}
