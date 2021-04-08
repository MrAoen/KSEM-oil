package com.ksem.oil.controllers;

import com.ksem.oil.topicServer.api.TopicServer;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@AllArgsConstructor
@Slf4j
public class TopicController {

    private final TopicServer topicServer;

    @PostMapping("/add/{topicName}")
    public boolean addTopic(@PathVariable @NotNull String topicName) {
        return topicServer.addTopic(topicName);
    }

    @DeleteMapping("/remove/{topicName}")
    public boolean removeTopic(@PathVariable @NotNull String topicName) {
        return topicServer.removeTopic(topicName);
    }
}
