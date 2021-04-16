package com.ksem.oil.services;

import com.ksem.oil.domain.dto.TransportMessage;

public interface MessageProcessor<Type> {

    Type convertEntityFromMessage(TransportMessage message);
}
