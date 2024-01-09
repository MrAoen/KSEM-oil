package com.ksem.oil.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SalaryDto {
    String azs;
    UUID extId;
    LocalDateTime date;
    String employee;
    String position;
    Double liter;
    Double rate;
    @JsonProperty("sum_liter_rate")
    Double sumLiterRate;
    Double premium;
    @JsonProperty("total_salaries")
    Double totalSalaries;
    @JsonProperty("prepayment_cashless")
    Double prepaymentCashless;
    @JsonProperty("prepayment_cash")
    Double prepaymentCash;
    @JsonProperty("pay_out")
    Double payOut;
    String comment;
    @JsonProperty("row_number")
    int rowNumber;
    @JsonProperty("row_count")
    int rowCount;
}
