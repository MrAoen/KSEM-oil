package com.ksem.oil.topicServer.api.impl;

import com.ksem.oil.domain.entity.TopicEntryEntity;
import com.ksem.oil.domain.repository.TopicEntryEntityRepository;
import com.ksem.oil.topicServer.TopicEntry;
import com.ksem.oil.topicServer.Topics;
import com.ksem.oil.topicServer.api.TopicServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class DynamicServer implements TopicServer {

    private final Topics topics;
    private final TopicEntryEntityRepository topicEntityRepository;
    private final ModelMapper modelMapper;

    public DynamicServer(TopicEntryEntityRepository topicEntityRepository, ModelMapper modelMapper) {
        this.topicEntityRepository = topicEntityRepository;
        this.modelMapper = modelMapper;
        this.topics = new Topics();
    }

    @Override
    public boolean addTopic(String topicName) {
        if (topics.getTopicEntries().stream().noneMatch(p -> Objects.equals(p.getName(), topicName))) {
            TopicEntry entry = new TopicEntry();
            entry.setName(topicName);
            new NewTopic(topicName, 1, (short) 1);
            topics.getTopicEntries().add(entry);
            return true;
        }
        log.warn("Topic already exist! %s", topicName);
        return false;
    }

    @Override
    @EventListener
    public boolean removeTopic(String topicName) {
        for (TopicEntry entry : topics.getTopicEntries()) {
            if (Objects.equals(entry.getName(), topicName)) {
                topics.getTopicEntries().remove(entry);
                Optional<TopicEntryEntity> opt = topicEntityRepository.findByName(entry.getName());
                if (opt.isPresent())
                    topicEntityRepository.delete(opt.get());
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
        TopicEntry topicEntry = topics.getTopicEntries().stream().filter(p -> Objects.equals(p.getName(), topicName)).findFirst().get();
        topicEntry.setCount(topicEntry.getCount() + 1);
    }

    @PostConstruct
    private void initalizeEntities() {
        List<TopicEntryEntity> actualTopics = topicEntityRepository.findAll();
        actualTopics.stream().forEach(record -> this.addTopic(record.getName()));
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
