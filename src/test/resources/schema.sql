CREATE TABLE customers
(
  customer_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  first_name VARCHAR(20) NOT NULL,
  last_name_kana VARCHAR(20) NOT NULL,
  first_name_kana VARCHAR(20) NOT NULL,
  email VARCHAR(50) NOT NULL,
  phone_number VARCHAR(13) NOT NULL
);

CREATE TABLE customer_addresses
(
  address_id INT PRIMARY KEY NOT NULL,
  customer_id INT NOT NULL,
  postal_code VARCHAR(8) NOT NULL,
  prefecture VARCHAR(10) NOT NULL,
  city VARCHAR(20) NOT NULL,
  town_and_number VARCHAR(50) NOT NULL,
  building_name_and_room_number VARCHAR(50)
);

CREATE TABLE vehicles
(
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    plate_region VARCHAR(10),
    plate_category_number INT,
    plate_hiragana VARCHAR(1),
    plate_vehicle_number INT NOT NULL,
    make VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    type VARCHAR(32) NOT NULL,
    year VARCHAR(32) NOT NULL,
    inspection_date DATE NOT NULL,
    CONSTRAINT fk_vehicles_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE maintenance_info
(
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    car_name VARCHAR(32) NOT NULL,
    oil_viscosity VARCHAR(32) NOT NULL,
    oil_quantity_with_filter DOUBLE NOT NULL,
    oil_quantity_without_filter DOUBLE NOT NULL,
    oil_filter_part_number VARCHAR(32) NOT NULL,
    car_wash_size VARCHAR(32) NOT NULL,
    CONSTRAINT fk_maintenance_info_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

CREATE TABLE estimate_base
(
    estimate_id INT AUTO_INCREMENT PRIMARY KEY,
    maintenance_id INT NOT NULL,
    CONSTRAINT fk_estimate_base_maintenance_id FOREIGN KEY (maintenance_id) REFERENCES maintenance_info(maintenance_id)
);

CREATE TABLE product_category
(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE products
(
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    CONSTRAINT fk_products_category_id FOREIGN KEY (category_id) REFERENCES product_category(category_id)
);

CREATE TABLE estimate_product
(
    estimate_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity DOUBLE NOT NULL,
    CONSTRAINT fk_estimate_product_estimate_id FOREIGN KEY (estimate_id) REFERENCES estimate_base(estimate_id),
    CONSTRAINT fk_estimate_product_product_id FOREIGN KEY (product_id) REFERENCES products(product_id)
);
