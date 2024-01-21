package ru.sbrf.stock.exchange.service.worker;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.stock.exchange.domain.OrderKafkaMessage;
import ru.sbrf.stock.exchange.domain.Status;
import ru.sbrf.stock.exchange.service.OrderInfoService;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class Worker {
    private static final Random RANDOM = new Random();
    private final OrderInfoService orderInfoService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    @Value("${spring.kafka.topic-name:order-topic}")
    private String topic;

    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        BlockingQueue<UUID> taskQueue = new LinkedBlockingQueue<>();
        IntStream.rangeClosed(1, 3)
                .boxed()
                .map(i -> new Thread(() -> produce(taskQueue)))
                .forEach(Thread::start);
        IntStream.rangeClosed(1, 5)
                .boxed()
                .map(i -> new Thread(() -> consume(taskQueue)))
                .forEach(Thread::start);

    }

    public void produce(BlockingQueue<UUID> taskQueue) {
        for (int i = 0; i < 1000; i++) {
            var uuid = orderInfoService.create();
            try {
                taskQueue.put(uuid);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void consume(BlockingQueue<UUID> taskQueue) {
        while (true) {
            try {
                var task = taskQueue.take();
                Thread.sleep(RANDOM.nextInt(100, 1000));
                processTask(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @SneakyThrows
    public void processTask(UUID uuid) {
        for (int i = 0; i < RANDOM.nextInt(8); i++) {
            restTemplate.put("http://localhost:8080/" + uuid + "/COMPLETED_" + i, null);
        }
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), new OrderKafkaMessage(uuid, Status.COMPLETED_KAFKA));
    }
}
