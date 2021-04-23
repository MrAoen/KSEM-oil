package com.ksem.oil.domain.dto;

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
    Long costItem;
    Double sum;
    UUID extId;
}
