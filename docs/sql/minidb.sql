USE minidb;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);
		
CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    stock INT NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Order_Item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    order_quantity INT NOT NULL,
    order_price INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

INSERT INTO Users (login_id, password, name, role) VALUES
('admin', 'admin123', '관리자', 'ADMIN'),
('user1', 'pass1', '김철수', 'USER'),
('user2', 'pass2', '이영희', 'USER'),
('user3', 'pass3', '박민수', 'USER'),
('user4', 'pass4', '최지훈', 'USER');

INSERT INTO Product (product_name, price, stock) VALUES
('노트북', 1200000, 10),
('마우스', 20000, 100),
('키보드', 50000, 80),
('모니터', 300000, 30),
('USB', 10000, 200),
('외장하드', 150000, 40),
('이어폰', 30000, 70),
('헤드셋', 80000, 50),
('스마트폰', 900000, 20),
('태블릿', 600000, 25),
('충전기', 15000, 150),
('케이블', 5000, 300),
('웹캠', 70000, 40),
('마이크', 120000, 20),
('스피커', 100000, 35),
('프린터', 200000, 15),
('의자', 150000, 20),
('책상', 250000, 10),
('램프', 40000, 60),
('선풍기', 80000, 30),
('에어컨', 1000000, 5),
('히터', 90000, 25),
('공기청정기', 300000, 12),
('가습기', 120000, 18),
('TV', 1500000, 8),
('냉장고', 2000000, 5),
('세탁기', 1800000, 6),
('전자레인지', 200000, 14),
('커피머신', 250000, 10),
('토스터', 70000, 22);

INSERT INTO Orders (user_id, status) VALUES
(1, '배송완료'), (2, '배송중'), (3, '주문완료'),
(4, '배송완료'), (5, '주문완료'),
(1, '배송중'), (2, '배송완료'), (3, '배송중'),
(4, '주문완료'), (5, '배송완료'),
(1, '배송완료'), (2, '주문완료'), (3, '배송완료'),
(4, '배송중'), (5, '주문완료'),
(1, '배송완료'), (2, '배송중'), (3, '주문완료'),
(4, '배송완료'), (5, '배송중'),
(1, '주문완료'), (2, '배송완료'), (3, '배송중'),
(4, '주문완료'), (5, '배송완료'),
(1, '배송중'), (2, '주문완료'), (3, '배송완료'),
(4, '배송완료'), (5, '배송중');

INSERT INTO Order_Item (order_id, product_id, order_quantity, order_price) VALUES
(1, 1, 1, 1200000),
(2, 2, 2, 40000),
(3, 3, 1, 50000),	
(4, 4, 1, 300000),
(5, 5, 3, 30000),
(6, 6, 1, 150000),
(7, 7, 2, 60000),
(8, 8, 1, 80000),
(9, 9, 1, 900000),
(10, 10, 1, 600000),
(11, 11, 2, 30000),
(12, 12, 4, 20000),
(13, 13, 1, 70000),
(14, 14, 1, 120000),
(15, 15, 2, 200000),
(16, 16, 1, 200000),
(17, 17, 1, 150000),
(18, 18, 1, 250000),
(19, 19, 2, 80000),
(20, 20, 1, 80000),
(21, 21, 1, 1000000),
(22, 22, 2, 180000),
(23, 23, 1, 300000),
(24, 24, 1, 120000),
(25, 25, 1, 1500000),
(26, 26, 1, 2000000),
(27, 27, 1, 1800000),
(28, 28, 2, 400000),
(29, 29, 1, 250000),
(30, 30, 1, 70000);