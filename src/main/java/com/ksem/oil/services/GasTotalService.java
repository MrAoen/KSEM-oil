package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.GasTotalDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.GasTotal;
import com.ksem.oil.domain.repository.GasTotalsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasTotalService implements MessageProcessor<GasTotal> {

    private final GasTotalsRepository repository;

    private final AzsService azsService;

    private final ObjectMapper objectMapper;

    @Override
    public GasTotal convertEntityFromMessage(TransportMessage message) {
        GasTotal entity = null;

        try {
            GasTotalDto dto = objectMapper.readValue(message.getPayload(), GasTotalDto.class);
            Azs azs = azsService.getAzs(dto.getAzs()).orElse(null);
            entity = repository.getByAzsAndPeriodAndTank(azs,dto.getPeriod(), dto.getTank()).orElse(new GasTotal());
            entity.setPeriod(dto.getPeriod());
            entity.setQuantity(dto.getQuantity());
            entity.setAzs(azs);
            entity.setGasolineType(dto.getGasolineType());
            entity.setTank(dto.getTank());
            entity = repository.save(entity);

        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to GasTotalDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }
}
