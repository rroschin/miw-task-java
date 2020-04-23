DROP TABLE IF EXISTS item;

CREATE TABLE item
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1024) NOT NULL,
    price       INT DEFAULT 0
);

DROP TABLE IF EXISTS item_view_log;

CREATE TABLE item_view_log
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    item_id   INT       NOT NULL,
    view_time TIMESTAMP NOT NULL DEFAULT NOW(),

    FOREIGN KEY (item_id) REFERENCES item (id)
);
