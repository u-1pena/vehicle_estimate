CREATE TABLE users
(
    id INT PRIMARY KEY NOT NULL,
    account VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE user_details
(
    id INT PRIMARY KEY NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    first_name_kana VARCHAR(20) NOT NULL,
    last_name_kana VARCHAR(20) NOT NULL,
    birthday DATE NOT NULL,
    mobile_phone_number VARCHAR(13) NOT NULL,
    password VARCHAR(16) NOT NULL
);

CREATE TABLE user_payments
(
    id INT PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    card_number VARCHAR(255) UNIQUE NOT NULL,
    card_brand VARCHAR(32) NOT NULL,
    card_holder VARCHAR(64) NOT NULL,
    expiration_date VARCHAR(7) NOT NULL
);
