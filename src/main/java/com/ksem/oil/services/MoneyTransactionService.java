package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.MoneyTransactionDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.MoneyTransaction;
import com.ksem.oil.domain.repository.MoneyTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MoneyTransactionService implements MessageProcessor<MoneyTransaction> {

    private final MoneyTransactionRepository moneyTransactionRepository;
    private final AzsService azsService;
    private final Global2LocalService global2LocalService;
    private final CustomerService customerService;

    @Override
    public MoneyTransaction convertEntityFromMessage(TransportMessage message) {
        MoneyTransaction entity = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MoneyTransactionDto record = objectMapper.readValue(message.getPayload(), MoneyTransactionDto.class);
            if (record.getExtId() == null) return null;
            entity = moneyTransactionRepository.findByExtId(record.getExtId()).orElse(new MoneyTransaction());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(record.getAzs()).orElse(null));
            entity.setDate(record.getDate());
            entity.setExtId(record.getExtId());
            entity.setSum(record.getSum());
            entity.setShift(record.getShift());
            entity.setSales_type(record.getSales_type());
            entity.setCostItem(record.getCostItem());
            if (entity.getAzs() != null) {
                entity.setGlobalSalesType(global2LocalService.localToGlobal(entity.getAzs(), record.getSales_type()));
                entity.setCustomer(customerService.getCustomer(record.getCustomer(), entity.getAzs()));
            }
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to MoneyTransactionDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }
}
