package com.ksem.oil.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportMessage {

    String type;
    String message;
    String payload;
    Long index;
}
