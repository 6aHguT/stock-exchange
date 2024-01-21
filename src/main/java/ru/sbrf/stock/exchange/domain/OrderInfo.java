package ru.sbrf.stock.exchange.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "domainEvents")
public class OrderInfo {
    @Transient
    private final Collection<DomainEvent> domainEvents = new LinkedList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;
    private LocalDateTime created = LocalDateTime.now();

    @SneakyThrows
    public void setStatus(Status status) {
        var domainEvent = new DomainEvent(this, getStatus(), status, LocalDateTime.now());
        this.status = status;
        domainEvents.add(domainEvent);
    }

    @DomainEvents
    public Collection<DomainEvent> events() {
        return domainEvents;
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }
}
