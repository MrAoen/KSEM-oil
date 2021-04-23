package com.ksem.oil.services;

import com.ksem.oil.domain.entity.Azs;
import com.ksem.oil.domain.entity.Global2LocalSalesType;
import com.ksem.oil.domain.repository.GlobalLocalTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class Global2LocalService {

    private final GlobalLocalTypeRepository globalLocalTypeRepository;

    public Long localToGlobal(Azs azs, Long localValue){
        Optional<Global2LocalSalesType> optVal = globalLocalTypeRepository.findByAzsAndLocalType(azs,localValue);
        if (optVal.isPresent()) return optVal.get().getGlobalType();
        else return Long.valueOf(-1);
    }

    public Long globalToLocal(Azs azs,Long globalValue){
        Optional<Global2LocalSalesType> optVal = globalLocalTypeRepository.findByAzsAndGlobalType(azs,globalValue);
        if(optVal.isPresent()) return optVal.get().getLocalType();
        else return Long.valueOf(-1);
    }

    public boolean save(Global2LocalSalesType entity){
        return globalLocalTypeRepository.save(entity) == null ? false : true;
    }
}
