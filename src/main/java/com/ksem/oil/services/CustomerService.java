package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.CustomerDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Customer;
import com.ksem.oil.domain.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService implements MessageProcessor<Customer> {

    private final CustomerRepository customerRepository;
    private final AzsService azsService;

    public Customer getCustomer(String name, Azs azs) {
        Customer result = null;
        Optional<Customer> optResult = customerRepository.findByNameAndAzs(name, azs);
        if (optResult.isEmpty()) {
            result = new Customer();
            result.setAzs(azs);
            result.setName(name);
            result = customerRepository.save(result);
        } else result = optResult.get();
        return result;
    }

    @Override
    public Customer convertEntityFromMessage(TransportMessage message) {
        Customer entity = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CustomerDto record = objectMapper.readValue(message.getPayload(), CustomerDto.class);
            if (record.getGlobalId() == null) return null;
            entity = customerRepository.findByGlobalId(record.getGlobalId()).orElse(new Customer());
            entity.setName(record.getName());
            entity.setGlobalId(record.getGlobalId());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(record.getAzs()).orElse(null));
            entity = customerRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to CustomerDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }
}
