package com.ksem.oil.controllers;

import com.ksem.oil.services.KafkaSender;
import com.ksem.oil.services.RemotePointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/message/")
@Slf4j
public class MessageSenderReciver {

    private final KafkaSender kafkaSender;
    private final RemotePointService remotePointService;

    public MessageSenderReciver(KafkaSender kafkaSender, RemotePointService remotePointService) {
        this.kafkaSender = kafkaSender;
        this.remotePointService = remotePointService;
    }

    @PutMapping("/{topicName}")
    public void push(@PathVariable @NotNull String topicName, @RequestBody String message) {
        kafkaSender.sendMessage(topicName, message);
    }

    @GetMapping("/{topicName}")
    public String readMessageByRemotePoint(@PathVariable @NotNull String topicName) {
        return remotePointService.checkBox(topicName);
    }

}
