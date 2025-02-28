CREATE DATABASE onlineshopping;
USE onlineshopping;

CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL,  -- 0 for User, 1 for Seller
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    quantity INT NOT NULL CHECK (quantity >= 0),
    retail_price DOUBLE NOT NULL CHECK (retail_price >= 0),
    wholesale_price DOUBLE NOT NULL CHECK (wholesale_price >= 0)
);

CREATE TABLE `order` (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date_placed DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    order_status VARCHAR(255) NOT NULL CHECK (order_status IN ('PROCESSING', 'COMPLETED', 'CANCELED')),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE order_item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    purchased_price DOUBLE NOT NULL CHECK (purchased_price >= 0),
    wholesale_price DOUBLE NOT NULL CHECK (wholesale_price >= 0),
    FOREIGN KEY (order_id) REFERENCES `order`(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

CREATE TABLE watchlist (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- Insert Users (Admin & Regular Users)
INSERT INTO user (email, password, role, username) VALUES
('user1@example.com', 'userpass1', 0, 'user1'),
('user2@example.com', 'userpass2', 0, 'user2'),
('seller@example.com', 'sellerpass', 1, 'seller');

-- Insert Products
INSERT INTO product (name, description, quantity, retail_price, wholesale_price) VALUES
('Laptop', 'High-performance laptop', 10, 1200.00, 900.00),
('Smartphone', 'Latest model smartphone', 15, 800.00, 600.00),
('Headphones', 'Noise-canceling headphones', 20, 150.00, 100.00),
('Smartwatch', 'Advanced fitness tracking', 12, 300.00, 200.00),
('Tablet', '10-inch display tablet', 8, 500.00, 350.00);
