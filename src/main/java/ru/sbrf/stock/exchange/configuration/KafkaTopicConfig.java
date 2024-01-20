package ru.sbrf.stock.exchange.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

//    @Value("${spring.kafka.topic-name:order-topic}")
//    private String topic;
//
//    @Value("${kafka_bootstrap_servers:localhost:9092}")
//    private String bootstrapServers;

//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        return new KafkaAdmin(Map.of("bootstrap.servers", bootstrapServers));
//    }
//
//    @Bean
//    public NewTopic topic() {
//        return new NewTopic(topic, 12, (short) 1);
//    }
}
