package com.ksem.oil.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ksem.oil.Constants;
import com.ksem.oil.domain.repository.TopicEntryEntityRepository;
import com.ksem.oil.topicServer.api.TopicServer;
import com.ksem.oil.topicServer.api.impl.DynamicServer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ksem.oil.domain.repository")
@ComponentScan(basePackages = {"com.ksem.oil"})
public class MainConfig {

    @Autowired
    ConcurrentKafkaListenerContainerFactory<String, String> factory;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public TopicServer getTopicServer(TopicEntryEntityRepository topicEntryEntityRepository){
        return new DynamicServer(topicEntryEntityRepository);
    }

    @Bean
    public ObjectMapper getJsonMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat(Constants.TRANSACTION_DATA_FORMAT);
        objectMapper.setDateFormat(df);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
