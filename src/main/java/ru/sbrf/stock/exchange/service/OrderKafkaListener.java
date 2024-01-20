package ru.sbrf.stock.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.sbrf.stock.exchange.service.worker.Worker;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {

    @Async
    @KafkaListener(topics = "order-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(Worker.OrderKafkaMessage message) {
        // Обработка сообщения из Kafka
        System.out.println("Received Kafka Message: " + message);
        // Здесь вы можете добавить логику для изменения сущности Order
    }
}
