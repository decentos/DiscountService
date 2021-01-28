drop table IF EXISTS discount_period;
drop table IF EXISTS discount_item;
drop table IF EXISTS discount_percent;
drop table IF EXISTS discount_user;
drop table IF EXISTS discount_configure;

create TABLE discount_period
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    start_date DATE    NOT NULL,
    end_date   DATE    NOT NULL,
    percent    INTEGER NOT NULL
);

create TABLE discount_percent
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    percent INTEGER NOT NULL
);

create TABLE discount_item
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    price               DOUBLE NOT NULL,
    base_price          DOUBLE NOT NULL,
    discount_percent_id BIGINT,
    discount_period_id  BIGINT,
    FOREIGN KEY (discount_percent_id) REFERENCES discount_percent (id) ON delete CASCADE,
    FOREIGN KEY (discount_period_id) REFERENCES discount_period (id) ON delete CASCADE
);

create TABLE discount_configure
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    sum_start DOUBLE     NOT NULL,
    sum_end   DOUBLE     NOT NULL,
    percent   INTEGER    NOT NULL,
    delete    TINYINT(1) NOT NULL DEFAULT 0
);

create TABLE discount_user
(
    id                    BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id               BIGINT NOT NULL,
    purchase_amount       DOUBLE NOT NULL,
    discount_configure_id BIGINT,
    FOREIGN KEY (discount_configure_id) REFERENCES discount_configure (id) ON delete CASCADE
);