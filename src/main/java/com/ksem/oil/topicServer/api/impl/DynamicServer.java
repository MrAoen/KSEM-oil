package com.ksem.oil.topicServer.api.impl;

import com.ksem.oil.domain.entity.TopicEntryEntity;
import com.ksem.oil.domain.repository.TopicEntryEntityRepository;
import com.ksem.oil.topicServer.TopicEntry;
import com.ksem.oil.topicServer.Topics;
import com.ksem.oil.topicServer.api.TopicServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.TopicBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class DynamicServer implements TopicServer {

    private final Topics topics;
    private final TopicEntryEntityRepository topicEntityRepository;

    public DynamicServer(TopicEntryEntityRepository topicEntityRepository) {
        this.topicEntityRepository = topicEntityRepository;
        this.topics = new Topics();
    }

    @Override
    public boolean addTopic(String topicName) {
        if (topics.getTopicEntries().stream().noneMatch(p -> Objects.equals(p.getName(), topicName))) {
            TopicEntry entry = new TopicEntry();
            entry.setName(topicName);
            //new NewTopic(topicName, 1, (short) 1);
            TopicBuilder.name(topicName)
                    .partitions(1)
                    .replicas(1)
                    .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                    .build();
            topics.getTopicEntries().add(entry);
            updateEntites();
            return true;
        }
        log.warn("Topic already exist! {}", topicName);
        return false;
    }

    @Override
    @EventListener
    public boolean removeTopic(String topicName) {
        for (TopicEntry entry : topics.getTopicEntries()) {
            if (Objects.equals(entry.getName(), topicName)) {
                topics.getTopicEntries().remove(entry);
                Optional<TopicEntryEntity> opt = topicEntityRepository.findByName(entry.getName());
                opt.ifPresent(topicEntityRepository::delete);
                log.info("Topic {} was removed", topicName);
                return true;
            }
        }
        log.warn("No topic {} found", topicName);
        return false;
    }

    @Override
    public boolean isPresent(String topicName) {
        return topics.getTopicEntries().stream().anyMatch(p -> Objects.equals(p.getName(), topicName));
    }

    @Override
    public List<TopicEntry> getList() {
        return topics.getTopicEntries();
    }

    @Override
    public void increment(String topicName) {
        Optional<TopicEntry> topicEntry = topics.getTopicEntries().stream().filter(p -> Objects.equals(p.getName(), topicName)).findFirst();
        topicEntry.ifPresent(entry -> entry.setCount(entry.getCount() + 1));
    }

    @PostConstruct
    private void initalizeEntities() {
        List<TopicEntryEntity> actualTopics = topicEntityRepository.findAll();
        actualTopics.forEach(topicEntry -> this.addTopic(topicEntry.getName()));
    }

    @PreDestroy
    private void updateEntites() {
        for (TopicEntry entry : topics.getTopicEntries()) {
            Optional<TopicEntryEntity> opt = topicEntityRepository.findByName(entry.getName());
            if (opt.isEmpty()) {
                TopicEntryEntity newBee = new TopicEntryEntity();
                newBee.setName(entry.getName());
                newBee = topicEntityRepository.save(newBee);
                if (newBee.getId() == null)
                    log.error("The topic {} not saved", entry.getName());
            }

        }
    }
}
