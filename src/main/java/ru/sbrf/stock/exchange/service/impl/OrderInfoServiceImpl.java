package ru.sbrf.stock.exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sbrf.stock.exchange.domain.OrderInfo;
import ru.sbrf.stock.exchange.domain.Status;
import ru.sbrf.stock.exchange.repository.OrderInfoRepository;
import ru.sbrf.stock.exchange.service.OrderInfoService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class OrderInfoServiceImpl implements OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;

    @Override
    public int cancelExpired() {
        var all = orderInfoRepository.findAllByStatus(Status.NEW);
        all.forEach(order -> order.setStatus(Status.CANCELED));
        return orderInfoRepository.saveAll(all).size();
    }

    @Override
    public Optional<OrderInfo> getById(UUID id) {
        return orderInfoRepository.findById(id);
    }

    @Override
    public UUID create() {
        var entity = new OrderInfo();
        orderInfoRepository.save(entity);
        return entity.getId();
    }

    @Override
    public Optional<UUID> update(UUID id, Status status) {
        return orderInfoRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderInfoRepository.save(order).getId();
                });
    }
}
