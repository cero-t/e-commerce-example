CREATE TABLE cart
(
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64),
  author VARCHAR(64),
  unit_price NUMBER,
  release DATE,
  image VARCHAR(512)
);
