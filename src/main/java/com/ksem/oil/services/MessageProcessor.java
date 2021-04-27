package com.ksem.oil.services;

import com.ksem.oil.domain.dto.TransportMessage;

public interface MessageProcessor<T> {

    T convertEntityFromMessage(TransportMessage message);
}
