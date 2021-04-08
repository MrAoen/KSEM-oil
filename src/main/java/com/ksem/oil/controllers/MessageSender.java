package com.ksem.oil.controllers;

import com.ksem.oil.services.KafkaSender;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/")
@AllArgsConstructor
public class MessageSender {

    private final KafkaSender kafkaSender;

    @PutMapping("/{topicName}")
    public void push(@PathVariable @NotNull String topicName, @RequestBody String message){
        kafkaSender.sendMessage(topicName, message);
    }
}
