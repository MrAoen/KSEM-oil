package com.ksem.oil.topicServer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicEntry {

    private String name;
    private int count = 0;
}
