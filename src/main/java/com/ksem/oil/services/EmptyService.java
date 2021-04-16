package com.ksem.oil.services;

import com.ksem.oil.domain.dto.TransportMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmptyService implements MessageProcessor<String>{

    @Override
    public String convertEntityFromMessage(TransportMessage message) {
        log.info("Empty processor[{}]:{}",message.getIndex(),message.getMessage());
        return null;
    }
}
