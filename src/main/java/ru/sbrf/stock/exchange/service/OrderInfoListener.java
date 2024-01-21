package ru.sbrf.stock.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.sbrf.stock.exchange.domain.DomainEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderInfoListener {

    private final OrderHistoryService orderHistoryService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DomainEvent.class)
    public void saveChanges(DomainEvent changes) {
//        log.debug(changes.toString());
        orderHistoryService.create(changes.order(), changes.oldStatus(), changes.newStatus(), changes.changed());
//        log.debug(history.toString());
    }

}