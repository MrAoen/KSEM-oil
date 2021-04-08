package com.ksem.oil.topicServer.api.impl;

import com.ksem.oil.topicServer.TopicEntry;
import com.ksem.oil.topicServer.Topics;
import com.ksem.oil.topicServer.api.TopicServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public class DynamicServer implements TopicServer {

    private final ConcurrentKafkaListenerContainerFactory<String, String> factory;
    private final Topics topics;

    public DynamicServer(ConcurrentKafkaListenerContainerFactory<String, String> factory) {
        this.factory = factory;
        this.topics = new Topics();
    }

    @Override
    public boolean addTopic(String topicName) {
        if (topics.getTopicEntries().stream().noneMatch(p -> Objects.equals(p.getName(), topicName))) {
            TopicEntry entry = new TopicEntry();
            entry.setName(topicName);
            ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(entry.getName());
            String groupId = UUID.randomUUID().toString();
            container.getContainerProperties().setGroupId(groupId);
            container.setupMessageListener((MessageListener) record -> {
                System.out.println(record);
            });
            entry.setTopicListener(container);
            topics.getTopicEntries().add(entry);
            container.start();

            return true;
        }
        log.warn("Topic already exist! %s",topicName);
        return false;
    }

    @Override
    @EventListener
    public boolean removeTopic(String topicName) {
        for (TopicEntry entry : topics.getTopicEntries()) {
            if(Objects.equals(entry.getName(),topicName)){
                entry.getTopicListener().stop();
                topics.getTopicEntries().remove(entry);
                log.info("Topic %s was removed",topicName);
                return true;
            }
        }
        log.warn("No topic %s found",topicName);
        return false;
    }
}
