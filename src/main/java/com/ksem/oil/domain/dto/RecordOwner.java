package com.ksem.oil.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class RecordOwner {
    String azs;
    UUID extId;
}
