package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.exceptions.InvalidMessage;
import com.ksem.oil.exceptions.InvalidMessageType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        JSONArray incomeObjects = new JSONArray();
        try {
            incomeObjects = new JSONArray(message);
        } catch (JSONException e) {
            log.error("Income structure in not JSONArray");
        }
        String errorCounter = "";
        for (int index = 0; index < incomeObjects.length(); index++) {
            //мусор выкидываем молча
            JSONObject obj = incomeObjects.optJSONObject(index);
            if (obj != null) {
                TransportMessage msg = null;
                try {
                    msg = objectMapper.readValue(obj.toString(), TransportMessage.class);
                    msg.setIndex(offset);
                    execMessageProcessor(msg);
                    msg.setIndex(offset);
                } catch (JsonProcessingException e) {
                    errorCounter = new StringBuilder(errorCounter).append(",").append(index).toString();
                }
            }
        }
        if (!errorCounter.isEmpty())
            throw new InvalidMessage(errorCounter);
    }

    private void execMessageProcessor(TransportMessage msg) {
        try {
            Class<?> clazz = Class.forName("com.ksem.oil.services." + msg.getType());
            Object service = context.getBean(clazz);
            Method method = Arrays.stream(clazz.getMethods()).filter(p -> p.getName().equals("convertEntityFromMessage")).findFirst().orElseThrow(NoSuchMethodException::new);
            method.invoke(service, msg);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidMessageType("com.ksem.oil.services" + msg.getType() + "Service.class");
        }
    }

}
