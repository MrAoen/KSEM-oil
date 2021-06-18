package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.MoneyTransactionDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.MoneyTransaction;
import com.ksem.oil.domain.repository.MoneyTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyTransactionService implements MessageProcessor<MoneyTransaction> {

    private final MoneyTransactionRepository moneyTransactionRepository;
    private final AzsService azsService;
    private final Global2LocalService global2LocalService;
    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    @Override
    public MoneyTransaction convertEntityFromMessage(TransportMessage message) {
        MoneyTransaction entity = null;
        try {
            MoneyTransactionDto recordDto = objectMapper.readValue(message.getPayload(), MoneyTransactionDto.class);
            if (recordDto.getExtId() == null) return null;
            entity = moneyTransactionRepository.findByExtId(recordDto.getExtId()).orElse(new MoneyTransaction());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(recordDto.getAzs()).orElse(null));
            entity.setDate(recordDto.getDate());
            entity.setExtId(recordDto.getExtId());
            entity.setSum(recordDto.getSum());
            entity.setShift(recordDto.getShift());
            entity.setSales_type(recordDto.getSales_type());
            entity.setCostItem(recordDto.getCostItem());
            entity.setWorker(recordDto.getWorker());
            if (entity.getAzs() != null) {
                entity.setGlobalSalesType(global2LocalService.localToGlobal(entity.getAzs(), recordDto.getSales_type()));
                entity.setCustomer(customerService.getCustomer(recordDto.getCustomer(), entity.getAzs()));
            }
            entity = moneyTransactionRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to MoneyTransactionDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }

    @Transactional
    public Integer delete(UUID extId) {
        return moneyTransactionRepository.deleteByExtId(extId);
    }
}
