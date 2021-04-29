package com.ksem.oil.domain.dto;

import com.ksem.oil.domain.entity.GasolineType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class GasSalesDto {
    String azs; //основной склад - наименование (или ГУИД, или струткуру строкой)
    GasolineType gasolineType;
    LocalDateTime date;
    int tank;
    int trk;
    Long sales_type;
    Long shift;
    Double liter;
    Double count;
    Double price;
    Double sum;
    Long check_number;
    UUID extId;//ГУИД на точке
    String customer;
}
