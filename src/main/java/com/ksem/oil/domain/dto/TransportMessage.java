package com.ksem.oil.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ksem.oil.utils.PayloadDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransportMessage {

    String type;
    String message;

    @JsonDeserialize(using = PayloadDeserializer.class)
    String payload;

    Long index;
    String sender;
}
