package ru.sbrf.stock.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderInfoService orderInfoService;

    @Async
    @Scheduled(cron = "${app.order-scheduler.cron:0 0/1 * * * ?}")
    @SchedulerLock(name = "${app.order-scheduler.name}", lockAtLeastFor = "PT30S", lockAtMostFor = "PT2M")
    public void scheduledTask() {
        log.debug("run task  {}", Thread.currentThread().getName());
        var cancelled = orderInfoService.cancelExpired();
        log.debug("cancelled, {}", cancelled);
    }
}
