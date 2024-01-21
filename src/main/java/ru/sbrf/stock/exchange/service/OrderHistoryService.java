package ru.sbrf.stock.exchange.service;

import jakarta.transaction.Transactional;
import ru.sbrf.stock.exchange.domain.OrderInfo;
import ru.sbrf.stock.exchange.domain.Status;

import java.time.LocalDateTime;

public interface OrderHistoryService {
    @Transactional
    void create(OrderInfo order, Status oldStatus, Status newStatus, LocalDateTime changed);
}
