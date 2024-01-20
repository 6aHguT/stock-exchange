package ru.sbrf.stock.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sbrf.stock.exchange.domain.Status;
import ru.sbrf.stock.exchange.service.OrderInfoService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderInfoController {
    private final OrderInfoService orderInfoService;

    @PostMapping
    public ResponseEntity<UUID> createOrder() {
        var savedOrder = orderInfoService.create();
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // Endpoint to update an existing order
    @PutMapping("/{id}/{status}")
    public ResponseEntity<UUID> updateOrder(@PathVariable UUID id, @PathVariable Status status) {
        return orderInfoService.update(id, status)
                .map(savedOrder -> new ResponseEntity<>(savedOrder, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
