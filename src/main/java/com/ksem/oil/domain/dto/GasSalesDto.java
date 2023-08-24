package com.ksem.oil.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("sales_type")
    Long salesType;
    Long shift;
    Double liter;
    Double count;
    Double price;
    Double sum;
    @JsonProperty("check_number")
    Long checkNumber;
    UUID extId;//ГУИД на точке
    String customer;
    String manager;
    String comment;
    @JsonProperty("row_number")
    int rowNumber;
    @JsonProperty("row_count")
    int rowCount;
    UUID uniqueIdOrder; //ГУИД заявки
}
n