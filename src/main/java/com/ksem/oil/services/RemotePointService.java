package com.ksem.oil.services;

import com.ksem.oil.topicServer.api.TopicServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RemotePointService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private final TopicServer topicServer;

    public RemotePointService(TopicServer topicServer) {
        this.topicServer = topicServer;
    }

    public String checkBox(String topicName) {
        JSONArray result = new JSONArray();
        JSONObject timestamp = new JSONObject();
        timestamp.put("message-timstamp", LocalDateTime.now());
        result.put(timestamp);
        if (topicServer.isPresent(topicName)) {
            KafkaConsumer<String, String> consumer = defaultConsumer(topicName);
            consumer.subscribe(Collections.singletonList(topicName));
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> row : records) {
                    JSONObject jObject = new JSONObject();
                    jObject.put(row.topic(), row.value());
                    result.put(jObject);
                    consumer.commitAsync();
                    topicServer.increment(topicName);
                }
            } catch (Exception e) {
                log.error("Exception while reading kafka messages for {} with message {}", topicName, e.getMessage());
            } finally {
                try {
                    consumer.commitSync();
                } finally {
                    consumer.close();
                }
            }
            return result.toString();
        }
        JSONObject error = new JSONObject();
        error.put("error", "No registred topic");
        result.put(error);
        return result.toString();
    }

    public KafkaConsumer<String, String> defaultConsumer(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<String, String>(props);
    }
}
