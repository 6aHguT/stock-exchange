package ru.sbrf.stock.exchange.domain;

import java.time.LocalDateTime;

public record DomainEvent(OrderInfo order, Status oldStatus, Status newStatus, LocalDateTime changed) {
}
