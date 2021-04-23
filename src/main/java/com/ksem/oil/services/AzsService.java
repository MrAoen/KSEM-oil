package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.AzsDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.repository.AzsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AzsService implements MessageProcessor<Azs> {

    private final AzsRepository azsRepository;

    public Optional<Azs> getAzs(String name) {
        return azsRepository.findByName(name);
    }

    public Optional<Azs> getAzs(UUID id) {
        return azsRepository.findByExtId(id);
    }

    @Override
    public Azs convertEntityFromMessage(TransportMessage message) {
        Azs result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AzsDto dto = objectMapper.readValue(message.getPayload(), AzsDto.class);
            if (dto.getExtId() == null) {
                if (dto.getName() == null) return result;
                else {
                    result = getAzs(dto.getName()).orElse(new Azs());
                }
            } else {
                result = getAzs(dto.getExtId()).orElse(new Azs());
            }
            boolean changed = false;
            if (!Objects.equals(result.getName(), dto.getName())) {
                result.setName(dto.getName());
                changed = true;
            }
            if (result.getExtId() == null) {
                result.setExtId(dto.getExtId());
                changed = true;
            }
            if (Boolean.TRUE.equals(changed)) {
                result = azsRepository.save(result);
            }

        } catch (JsonProcessingException e) {
            log.error("wrong AZS dto data in index {}", message.getIndex());
            return null;
        }
        return result;
    }

}
