package com.ksem.oil.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.RecordOwner;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.domain.entity.GasSales;
import com.ksem.oil.domain.entity.MoneyTransaction;
import com.ksem.oil.domain.repository.GasSalesRepository;
import com.ksem.oil.domain.repository.MoneyTransactionRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"com.ksem.oil"})
class DeleteDocumentRecordsTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GasSalesRepository gasSalesRepository;

    @Autowired
    MoneyTransactionRepository moneyTransactionRepository;

    @Autowired
    DeleteDocumentRecords deleteDocumentRecords;

    public final UUID extId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        GasSales gas = new GasSales();
        gas.setCount(1.5);
        gas.setExtId(extId);
        gas.setDate(LocalDateTime.now());

        gasSalesRepository.save(gas);

        MoneyTransaction mt = new MoneyTransaction();
        mt.setExtId(extId);
        mt.setDate(LocalDateTime.now());
        mt.setSum(1000.00);
        moneyTransactionRepository.save(mt);
    }

    @SneakyThrows
    @Test
    void convertEntityFromMessage() {
        assertEquals(1,moneyTransactionRepository.findAllByExtId(extId).size());
        assertEquals(1,gasSalesRepository.findAllByExtId(extId).size());

        TransportMessage msg = new TransportMessage();
        msg.setType("DeleteDocumentService");

        RecordOwner ro = new RecordOwner();
        ro.setExtId(extId);
        ro.setAzs("test");

        msg.setPayload(objectMapper.writeValueAsString(ro));
        deleteDocumentRecords.convertEntityFromMessage(msg);

        assertEquals(0,moneyTransactionRepository.findAllByExtId(extId).size());
        assertEquals(0,gasSalesRepository.findAllByExtId(extId).size());
    }
}