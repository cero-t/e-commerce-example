CREATE TABLE order
(
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(256),
  address VARCHAR(512),
  telephone VARCHAR(16),
  mail_address VARCHAR(256),
  card_number VARCHAR(32),
  card_expire VARCHAR(8),
  card_name VARCHAR(256),
  cart_id VARCHAR(32)
);
