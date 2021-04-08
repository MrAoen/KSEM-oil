package com.ksem.oil.topicServer;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;

@Getter
@Setter
public class TopicEntry {

    private String name;
    private AbstractMessageListenerContainer<String, String> topicListener;
    private NewTopic topic;
}
