package ru.sbrf.stock.exchange.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.sbrf.stock.exchange.domain.DomainEvent;
import ru.sbrf.stock.exchange.domain.OrderHistory;
import ru.sbrf.stock.exchange.repository.OrderHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DomainEvent.class)
    public void saveChanges(DomainEvent changes) {
//        log.debug(changes.toString());
        var history = new OrderHistory();
        history.setOrder(changes.order());
        history.setOldStatus(changes.oldStatus());
        history.setNewStatus(changes.newStatus());
        history.setChanged(changes.changed());
        orderHistoryRepository.save(history);
//        log.debug(history.toString());
    }
}
