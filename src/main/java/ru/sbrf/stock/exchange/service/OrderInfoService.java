package ru.sbrf.stock.exchange.service;

import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.stock.exchange.domain.OrderInfo;
import ru.sbrf.stock.exchange.domain.Status;

import java.util.Optional;
import java.util.UUID;

public interface OrderInfoService {

    int cancelExpired();

    Optional<OrderInfo> getById(UUID id);

    @Transactional
    UUID create();

    @Retryable
    @Transactional
    Optional<UUID> update(UUID id, Status status);

}
