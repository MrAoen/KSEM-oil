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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasSalesService implements MessageProcessor<GasSales> {

    private final GasSalesRepository gasSalesRepository;
    private final AzsService azsService;
    private final Global2LocalService global2LocalService;
    private final ObjectMapper objectMapper;
    private final CustomerService customerService;

    @Override
    public GasSales convertEntityFromMessage(TransportMessage message) {
        GasSales entity = null;

        try {
            GasSalesDto salesDto = objectMapper.readValue(message.getPayload(), GasSalesDto.class);
            if (salesDto.getExtId() == null) return null;
            List<GasSales> existingRows = gasSalesRepository.findAllByExtId(salesDto.getExtId());
            existingRows.forEach(r -> {
                if (r.getRowNumber() > salesDto.getRowCount()) {
                    gasSalesRepository.delete(r);
                }
            });
            entity = gasSalesRepository.findByExtIdAndRowNumber(salesDto.getExtId(), salesDto.getRowNumber())
                    .orElse(new GasSales());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(salesDto.getAzs()).orElse(null));
            entity.setCount(salesDto.getCount());
            entity.setCheckNumber(salesDto.getCheckNumber());
            entity.setExtId(salesDto.getExtId());
            entity.setGasolineType(salesDto.getGasolineType());
            entity.setLiter(salesDto.getLiter());
            entity.setPrice(salesDto.getPrice());
            entity.setSales_type(salesDto.getSalesType());
            entity.setShift(salesDto.getShift());
            if(salesDto.getSum() == null){
                entity.setSum(0.0);
            }else {
                entity.setSum(salesDto.getSum());
            }
            entity.setTank(salesDto.getTank());
            entity.setTrk(salesDto.getTrk());
            entity.setDate(salesDto.getDate());
            entity.setManager(salesDto.getManager());
            entity.setComment(salesDto.getComment());
            entity.setRowNumber(salesDto.getRowNumber());
            entity.setUniqueIdOrder(salesDto.getUniqueIdOrder());
            if (entity.getAzs() != null) {
                entity.setCustomer(customerService.getCustomer(salesDto.getCustomer(), entity.getAzs()));
                entity.setGlobalSalesType(global2LocalService.localToGlobal(entity.getAzs(), salesDto.getSalesType()));
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

    @Transactional
    public Integer delete(UUID extId) {
        return gasSalesRepository.deleteByExtId(extId);
    }
}
