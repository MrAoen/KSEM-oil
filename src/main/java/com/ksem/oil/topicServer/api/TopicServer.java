package com.ksem.oil.topicServer.api;

import com.ksem.oil.topicServer.TopicEntry;

import java.util.List;

public interface TopicServer {

    boolean addTopic(String topicName);

    boolean removeTopic(String topicName);

    boolean isPresent(String topicName);

    List<TopicEntry> getList();

    void increment(String topicName);
}
