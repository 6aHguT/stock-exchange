package ru.sbrf.stock.exchange.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic-name:order-topic}")
    private String topic;

    @Bean
    public NewTopic topic() {
        return new NewTopic(topic, 12, (short) 1);
    }
}
