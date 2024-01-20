package ru.sbrf.stock.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sbrf.stock.exchange.domain.OrderInfo;
import ru.sbrf.stock.exchange.domain.Status;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, UUID> {
    List<OrderInfo> findAllByStatus(Status status);
}
