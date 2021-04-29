package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.GasSalesDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.GasSales;
import com.ksem.oil.domain.repository.GasSalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasSalesService implements MessageProcessor<GasSales> {

    private final GasSalesRepository gasSalesRepository;
    private final AzsService azsService;
    private final Global2LocalService global2LocalService;
    private final ObjectMapper objectMapper;

    @Override
    public GasSales convertEntityFromMessage(TransportMessage message) {
        GasSales entity = null;

        try {
            GasSalesDto record = objectMapper.readValue(message.getPayload(), GasSalesDto.class);
            if (record.getExtId() == null) return null;
            entity = gasSalesRepository.findByExtId(record.getExtId()).orElse(new GasSales());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(record.getAzs()).orElse(null));
            entity.setCount(record.getCount());
            entity.setCheckNumber(record.getCheck_number());
            entity.setExtId(record.getExtId());
            entity.setGasolineType(record.getGasolineType());
            entity.setLiter(record.getLiter());
            entity.setPrice(record.getPrice());
            entity.setSales_type(record.getSales_type());
            entity.setShift(record.getShift());
            entity.setSum(record.getSum());
            entity.setTank(record.getTank());
            entity.setTrk(record.getTrk());
            entity.setDate(record.getDate());
            if (entity.getAzs() != null) {
                entity.setGlobalSalesType(global2LocalService.localToGlobal(entity.getAzs(), record.getSales_type()));
            }
            return gasSalesRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to GasSalesDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }

    public List<GasSales> getDaySalesByAzs(Azs azs, LocalDateTime date) {
        return gasSalesRepository.findAllByAzsAndDateBetween(
                azs
                , date.with(ChronoField.NANO_OF_DAY, 0)
                , date.with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay())
        );
    }
}
