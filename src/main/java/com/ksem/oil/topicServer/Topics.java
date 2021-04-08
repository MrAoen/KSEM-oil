package com.ksem.oil.topicServer;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
public class Topics {

    @NotNull
    List<TopicEntry> topicEntries;

    public Topics() {
        this.topicEntries = new ArrayList<>();
    }
}
