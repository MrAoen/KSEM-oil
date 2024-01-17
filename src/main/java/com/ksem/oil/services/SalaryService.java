package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.SalaryDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.Salary;
import com.ksem.oil.domain.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalaryService implements MessageProcessor<Salary> {

    private final SalaryRepository salaryRepository;
    private final ObjectMapper objectMapper;
    private final AzsService azsService;


    @Override
    public Salary convertEntityFromMessage(TransportMessage message) {
        Salary entity = null;

        try {
            SalaryDto recordDto = objectMapper.readValue(message.getPayload(), SalaryDto.class);
            if (recordDto.getExtId() == null) return null;

            List<Salary> existingRows = salaryRepository.findAllByExtId(recordDto.getExtId());
            existingRows.forEach(r -> {
                if (r.getRowNumber() > recordDto.getRowCount()) {
                    salaryRepository.delete(r);
                }
            });

            entity = salaryRepository.findByExtIdAndRowNumber(recordDto.getExtId(), recordDto.getRowNumber()).orElse(new Salary());
            if (entity.getAzs() == null) entity.setAzs(azsService.getAzs(recordDto.getAzs()).orElse(null));

            entity.setExtId(recordDto.getExtId());
            entity.setDate(recordDto.getDate());
            entity.setEmployee(recordDto.getEmployee());
            entity.setPosition(recordDto.getPosition());
            entity.setLiter(recordDto.getLiter());
            entity.setRate(recordDto.getRate());
            entity.setSumLiterRate(recordDto.getSumLiterRate());
            entity.setPremium(recordDto.getPremium());
            entity.setTotalSalaries(recordDto.getTotalSalaries());
            entity.setPrepaymentCashless(recordDto.getPrepaymentCashless());
            entity.setPrepaymentCash(recordDto.getPrepaymentCash());
            entity.setPayOut(recordDto.getPayOut());
            entity.setComment(recordDto.getComment());
            entity.setRowNumber(recordDto.getRowNumber());

            entity = salaryRepository.save(entity);
        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to SalaryDto {} from {}", message.getIndex(), message.getSender());
        }
        return entity;
    }

    @Transactional
    public Integer delete(UUID extId) {
        return salaryRepository.deleteByExtId(extId);
    }
}
