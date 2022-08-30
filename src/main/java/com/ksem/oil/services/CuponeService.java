package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.CuponSalesDto;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.CuponSales;
import com.ksem.oil.domain.repository.CuponSalesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CuponeService implements MessageProcessor<List<CuponSales>> {

    private final ObjectMapper objectMapper;
    private final CuponSalesRepository cuponSalesRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CuponSales> convertEntityFromMessage(TransportMessage message) {
        List<CuponSales> entityList = new ArrayList<>();

        try {
            List<CuponSalesDto> dtoList = Arrays.asList(objectMapper.readValue(message.getPayload(), CuponSalesDto[].class));
            if(!dtoList.isEmpty()){
                UUID extId = dtoList.get(0).getExtId();
                entityList = cuponSalesRepository.getByExtId(extId);
                for (CuponSales ckeckItem :entityList) {
                    if(!dtoList.stream().anyMatch(p-> p.getNumber().equals(ckeckItem.getNumber()))){
                        cuponSalesRepository.delete(ckeckItem);
                    }
                }
            }

            for (CuponSalesDto dto : dtoList) {
                var entityItem = entityList.stream().filter(p-> p.getNumber().equals(dto.getNumber())).findFirst().orElse(new CuponSales());
                modelMapper.map(dto,entityItem);
                cuponSalesRepository.save(entityItem);
            }

        } catch (JsonProcessingException e) {
            log.error("Can't convert payload to RecordOwner {} from {}", message.getIndex(), message.getSender());
        }
        return entityList;
    }
}
