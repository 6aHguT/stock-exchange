package ru.sbrf.stock.exchange.service.worker;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

//    public List<UUID> populate(int size) {
//        var executorService = Executors.newWorkStealingPool();
//        var tasks = IntStream.rangeClosed(1, size)
//                .boxed()
//                .map(i -> (Callable<UUID>) orderInfoService::create)
//                .toList();
//        List<Future<UUID>> futures = null;
//        try {
//            futures = executorService.invokeAll(tasks);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        executorService.shutdown();
//
//        return futures.stream()
//                .map(future -> {
//                    try {
//                        return future.get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .toList();
//
//    }
    private final KafkaTemplate<String, OrderKafkaMessage> kafkaTemplate;
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

    @Async
    public void produce(BlockingQueue<UUID> taskQueue) {
        for (int i = 0; i < 1000; i++) {
            var uuid = orderInfoService.create();
            try {
                taskQueue.put(uuid);
//                Thread.sleep(RANDOM.nextInt(100, 500)); // Небольшая задержка для демонстрации
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
    @Async
    public void processTask(UUID uuid) {
        for (int i = 0; i < RANDOM.nextInt(3); i++) {
            restTemplate.put("http://localhost:8080/" + uuid + "/COMPLETED", null);
        }
        sendMessage(new OrderKafkaMessage(uuid, Status.COMPLETED));
    }

    @Async
    public void sendMessage(OrderKafkaMessage message) {
        new Thread(() -> {
            var send = kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
            var val = send.join();
            System.out.println(val);
        }).start();
        System.out.println("message");
    }

    public record OrderKafkaMessage(UUID orderId, Status status) {
    }
}
