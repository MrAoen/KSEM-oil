package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.MessageDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Message;
import com.ksem.oil.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService implements MessageProcessor<Message> {

    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private final AzsService azsService;

    @Override
    public Message convertEntityFromMessage(TransportMessage message) {
        Message result = null;
        try {
            var dto = objectMapper.readValue(message.getPayload(), MessageDto.class);
            var entity = new Message();
            entity.setName(dto.getName());
            entity.setCode(dto.getCode());
            entity.setDate(LocalDateTime.now());
            entity.setValue(dto.getValue());
            var azs = azsService.getAzs(dto.getAzs());
            if(azs.isPresent()) entity.setAzs(azs.get());
            else log.warn("No azs found by name {} with message service",dto.getAzs());
            result = messageRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("wrong Message dto data in index {}", message.getIndex());
        }
        return result;
    }
}
