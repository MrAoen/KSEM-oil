package com.ksem.oil.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MoneyTransactionDto {
    String azs;
    LocalDateTime date;
    String customer;
    Long shift;
    Long sales_type;
    Long globalSalesType;
    UUID costItem;
    Double sum;
    UUID extId;
    UUID worker;
    @JsonProperty("row_number")
    int rowNumber;
    @JsonProperty("row_count")
    int rowCount;
}
