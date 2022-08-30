package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.RecordOwner;
import com.ksem.oil.domain.dto.TransportMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteDocumentRecords implements MessageProcessor<RecordOwner> {

    private final GasSalesService gasSalesService;
    private final MoneyTransactionService moneyTransactionService;
    private final ObjectMapper objectMapper;
    //TODO add deletetion for cupones (or no)

    @Override
    public RecordOwner convertEntityFromMessage(TransportMessage message) {
        RecordOwner dto = null;
        try {
            dto = objectMapper.readValue(message.getPayload(), RecordOwner.class);
            if (dto.getExtId() != null) {
                var deleted = gasSalesService.delete(dto.getExtId());
                var deletedM = moneyTransactionService.delete(dto.getExtId());
                if(deleted-deletedM != 0 ) log.warn("deleted different rows from source: {} from sales and {} from money",deleted,deletedM);
            }
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to RecordOwner {} from {}", message.getIndex(), message.getSender());
        }
        return dto;
    }
}
