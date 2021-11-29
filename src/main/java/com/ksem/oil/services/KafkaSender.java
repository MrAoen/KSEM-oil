package com.ksem.oil.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topicName, String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("pkg on {} for topic=[{}] with offset=[{}] strts with={}",
                        result.getRecordMetadata().timestamp(),
                        topicName,
                        result.getRecordMetadata().offset(),
                        message.substring(0, 32));
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("err on {} for topic=[{}] message strts with={}",
                        LocalDateTime.now(),
                        topicName,
                        message.substring(0, 32));
            }
        });
    }
}
