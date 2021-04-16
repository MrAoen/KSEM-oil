package com.ksem.oil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.services.KafkaSender;
import com.ksem.oil.services.RemotePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("dev")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class OilApplicationTests {

    @Autowired
    KafkaSender kafkaSender;

    @Autowired
    RemotePointService remotePointService;

    private final static String testTopic = "azs1";

    private TransportMessage msg;
    private String jsonMessage;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        msg = new TransportMessage();
        msg.setMessage("Hello operator");
        msg.setType("EmptyService");
        msg.setPayload("This is message payload");
        jsonMessage = mapper.writeValueAsString(msg);
    }

    @Test
    void contextLoads() throws JSONException {
        kafkaSender.sendMessage(testTopic, jsonMessage);
        String result = remotePointService.checkBox(testTopic);
        assertNotNull(result);
    }

}
