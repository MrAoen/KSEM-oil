package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.CustomerDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Customer;
import com.ksem.oil.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements MessageProcessor<Customer> {

    private final CustomerRepository customerRepository;
    private final AzsService azsService;
    private final ObjectMapper objectMapper;

    public Customer getCustomer(String name, Azs azs) {
        Customer result = new Customer();
        List<Customer> optResult = customerRepository.findByNameAndAzs(name, azs);
        if (optResult.isEmpty()) {
            result.setAzs(azs);
            result.setName(name);
            result = customerRepository.save(result);
            return result;
        } else {
            //find first with nonNull extid
            for (Customer it:optResult) {
                if(it.getGlobalId() != null){
                    result = it;
                    break;
                }else{
                    //if no customer with globalId but exist
                    result = it;
                }
            }
        }
        return result;
    }

    @Override
    public Customer convertEntityFromMessage(TransportMessage message) {
        Customer entity = null;
        try {
            CustomerDto recordDto = objectMapper.readValue(message.getPayload(), CustomerDto.class);
            if(recordDto.getName().isEmpty()) return null;
            if (recordDto.getGlobalId() == null) return null;
            if (recordDto.getAzs().isEmpty()) return null;
            var azs = azsService.getAzs(recordDto.getAzs()).orElse(null);
            if(recordDto.getGlobalId() != null) {
                entity = customerRepository.findByGlobalIdAndAzs(recordDto.getGlobalId(), azs).orElse(new Customer());
            }else{
                entity = customerRepository.findByNameAndAzs(recordDto.getName(), azs).stream().findFirst().orElse(new Customer());
            }
            entity.setName(recordDto.getName());
            entity.setGlobalId(recordDto.getGlobalId());
            entity.setAzs(azs);
            entity = customerRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to CustomerDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }
}
