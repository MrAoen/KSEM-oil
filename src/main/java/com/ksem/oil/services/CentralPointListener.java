package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.exceptions.InvalidMessage;
import com.ksem.oil.exceptions.InvalidMessageType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


@Component
@Slf4j
public class CentralPointListener {

    @Autowired
    ApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = "AZS2Central")
    public void pollCentralMessages(@Payload String message,
                                    @Header(KafkaHeaders.OFFSET) Long offset
    ) {

        var incomeObjects = new JSONArray();
        try {
            incomeObjects = new JSONArray(message);
        } catch (JSONException e) {
            log.error("Income structure in not JSONArray");
        }
        var errorCounter = "";
        for (var index = 0; index < incomeObjects.length(); index++) {
            //мусор выкидываем молча
            var obj = incomeObjects.optJSONObject(index);
            if (obj != null) {
                TransportMessage msg = null;
                try {
                    msg = objectMapper.readValue(obj.toString(), TransportMessage.class);
                    if (msg != null) {
                        msg.setIndex(offset);
                        execMessageProcessor(msg);
                        msg.setIndex(offset);
                    }
                } catch (JsonProcessingException e) {
                    errorCounter = errorCounter + "," + index;
                }
            }
        }
        if (!errorCounter.isEmpty())
            throw new InvalidMessage(errorCounter);
    }

    private void execMessageProcessor(TransportMessage msg) {
        var packageName = "com.ksem.oil.services.";
        try {
            if (msg == null || msg.toString().isEmpty()) {
                log.warn("empty message appear.");
                return;
            }
            Class<?> clazz = Class.forName(packageName + msg.getType());
            Object service = context.getBean(clazz);
            var method = Arrays.stream(clazz.getMethods()).filter(p -> p.getName().equals("convertEntityFromMessage")).findFirst().orElseThrow(NoSuchMethodException::new);
            method.invoke(service, msg);
        } catch (ClassNotFoundException e) {
            throw new InvalidMessageType(packageName + msg.getType() + ".class not found! with message:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new InvalidMessageType(packageName + msg.getType() + ".class method convertEntityFromMessage not found! with message:" + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new InvalidMessageType(packageName + msg.getType() + ".class illegal access to method with message:" + e.getMessage());
        } catch (InvocationTargetException e) {
            log.warn("Message with trowble:", msg);
            throw new InvalidMessageType(packageName + msg.getType() + " original message:" + msg.toString() + ".class invoc exception with message:" + e.getMessage());
        }
    }

}
