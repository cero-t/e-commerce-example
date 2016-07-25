CREATE TABLE payment
(
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  card_number VARCHAR(32),
  expire VARCHAR(8),
  name VARCHAR(256),
  amount NUMBER
);
