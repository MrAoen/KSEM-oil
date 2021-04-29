package com.ksem.oil.controllers;

import com.ksem.oil.topicServer.TopicEntry;
import com.ksem.oil.topicServer.api.TopicServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/config")
@AllArgsConstructor
@Slf4j
public class TopicController {

    private final TopicServer topicServer;

    @PostMapping("/{topicName}")
    public boolean addTopic(@PathVariable @NotNull String topicName) {
        return topicServer.addTopic(topicName);
    }

    @DeleteMapping("/{topicName}")
    public boolean removeTopic(@PathVariable @NotNull String topicName) {
        return topicServer.removeTopic(topicName);
    }

    @GetMapping("/list")
    public List<TopicEntry> getTopicList(){
        return topicServer.getList();
    }
}
