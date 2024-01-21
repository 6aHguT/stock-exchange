package ru.sbrf.stock.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.sbrf.stock.exchange.domain.OrderKafkaMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaListener {

    private final OrderInfoService orderInfoService;

    @Async
    @KafkaListener(topics = "order-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(OrderKafkaMessage message) {
//        log.debug("Received Kafka Message: " + message);
        orderInfoService.update(message.orderId(), message.status());
    }
}