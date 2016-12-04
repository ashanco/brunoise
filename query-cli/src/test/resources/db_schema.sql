DROP TABLE IF EXISTS customer;
CREATE TABLE customer(
  customer_id INT NOT NULL,
  customer_name VARCHAR(31) NOT NULL,
  PRIMARY KEY (customer_id)
);

DROP TABLE IF EXISTS sale;
CREATE TABLE sale(
  sale_id INT AUTO_INCREMENT,
  customer_id INT NOT NULL,
  amount INT NOT NULL,
  PRIMARY KEY (sale_id),
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO customer (customer_id, customer_name)
VALUES (299999, 'ashanco'), (800001, 'agdestein');

INSERT INTO sale (customer_id, amount)
VALUES (299999, 8500), (800001, 125000);

COMMIT;
