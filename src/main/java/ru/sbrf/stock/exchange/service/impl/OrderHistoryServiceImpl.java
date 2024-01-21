package ru.sbrf.stock.exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sbrf.stock.exchange.domain.OrderHistory;
import ru.sbrf.stock.exchange.domain.OrderInfo;
import ru.sbrf.stock.exchange.domain.Status;
import ru.sbrf.stock.exchange.repository.OrderHistoryRepository;
import ru.sbrf.stock.exchange.service.OrderHistoryService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public void create(OrderInfo order, Status oldStatus, Status newStatus, LocalDateTime changed) {
//        log.debug(changes.toString());
        orderHistoryRepository.save(new OrderHistory(UUID.randomUUID(), order, oldStatus, newStatus, changed));
//        log.debug(history.toString());
    }
}
