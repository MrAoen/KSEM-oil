package com.ksem.oil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.exceptions.InvalidMessage;
import com.ksem.oil.exceptions.InvalidMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;


@Component
@Slf4j
public class CentralPointListener {

    @Autowired
    ApplicationContext context;

    @KafkaListener(topics = "AZS-to-Central")
    public void pollCentralMessages(@Payload String message,
        @Header(KafkaHeaders.OFFSET) Long offset
        ) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.setDateFormat(df);
        objectMapper.registerModule(new JavaTimeModule());
        JSONArray incomeObjects = null;
        try {
            incomeObjects = new JSONArray(message);
        } catch (JSONException e) {
            log.error("Income structure in not JSONArray");
        }
        String errorCounter = "";
        for(int index = 0; index< incomeObjects.length(); index++){
            //мусор выкидываем молча
            JSONObject obj = incomeObjects.optJSONObject(index);
            if(obj != null){
                TransportMessage msg = null;
                try {
                    msg = objectMapper.readValue(obj.toString(),TransportMessage.class);
                    msg.setIndex(offset);
                    try {
                        Class clazz = Class.forName("com.ksem.oil.services." + msg.getType() );
                        Object service = context.getBean(clazz);
                        Method method = Arrays.stream(clazz.getMethods()).filter(p->p.getName().equals("convertEntityFromMessage")).findFirst().orElseThrow(NoSuchMethodException::new);
                        Object result = method.invoke(service,msg);
                        //TODO post process with resul object
                    }catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                        throw new InvalidMessageType("com.ksem.oil.services" + msg.getType() + "Service.class");
                    }
                } catch (JsonProcessingException e) {
                    errorCounter += ","+index;
                }
                msg.setIndex(offset);
            }
        }
        if(!errorCounter.isEmpty())
            throw new InvalidMessage(errorCounter);
    }

}
