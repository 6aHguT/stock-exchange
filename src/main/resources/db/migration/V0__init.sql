CREATE TABLE shedlock
(
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY (name)
);

CREATE TABLE order_info
(
    id      UUID PRIMARY KEY NOT NULL,
    status  VARCHAR          NOT NULL,
    created TIMESTAMP        NOT NULL
);

CREATE TABLE order_history
(
    id         UUID PRIMARY KEY NOT NULL,
    order_id   UUID             NOT NULL
        CONSTRAINT order_history_order_info_id_fk
            REFERENCES order_info (id),
    old_status VARCHAR          NOT NULL,
    new_status VARCHAR          NOT NULL,
    changed    TIMESTAMP        NOT NULL
);

