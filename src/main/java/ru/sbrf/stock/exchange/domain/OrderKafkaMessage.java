package ru.sbrf.stock.exchange.domain;

import java.util.UUID;

public record OrderKafkaMessage(UUID orderId, Status status) {
}
