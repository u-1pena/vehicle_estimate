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
  address_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  postal_code VARCHAR(8) NOT NULL,
  prefecture VARCHAR(10) NOT NULL,
  city VARCHAR(20) NOT NULL,
  town_and_number VARCHAR(50) NOT NULL,
  building_name_and_room_number VARCHAR(50),

  CONSTRAINT fk_customer_addresses_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
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
    active BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_vehicles_customer_id FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

CREATE TABLE maintenance_guides
(
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(32) NOT NULL,
    vehicle_name VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    type VARCHAR(32) NOT NULL,
    start_year VARCHAR(7) NOT NULL,
    end_year VARCHAR(7) NOT NULL,
    oil_viscosity VARCHAR(32) NOT NULL,
    oil_quantity_with_filter DOUBLE NOT NULL,
    oil_quantity_without_filter DOUBLE NOT NULL,
    oil_filter_part_number VARCHAR(32) NOT NULL,
    car_wash_size VARCHAR(32) NOT NULL
);

CREATE TABLE product_categories
(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(32) NOT NULL
);

CREATE TABLE products
(
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    product_name VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    guide_match_key VARCHAR(32) NOT NULL,
    price DECIMAL NOT NULL,
    CONSTRAINT fk_products_category_id FOREIGN KEY (category_id) REFERENCES product_categories(category_id)
);



CREATE TABLE estimate_bases (
    estimate_base_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    maintenance_id INT NOT NULL,
    estimate_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_estimate_base_maintenance_id FOREIGN KEY (maintenance_id)
    REFERENCES maintenance_guides(maintenance_id)
);

CREATE TABLE estimate_products (
    estimate_product_id INT AUTO_INCREMENT PRIMARY KEY,
    estimate_base_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity DOUBLE NOT NULL,
    unit_price DECIMAL(8, 2) NOT NULL,
    total_price DECIMAL(8, 2) NOT NULL,
    CONSTRAINT fk_estimate_products_estimate_base_id FOREIGN KEY (estimate_base_id)
    REFERENCES estimate_bases(estimate_base_id) ON DELETE CASCADE,
    CONSTRAINT fk_estimate_products_product_id FOREIGN KEY (product_id)
    REFERENCES products(product_id)
);

CREATE TABLE guide_product_permissions (
    maintenance_id INT NOT NULL,
    category_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity DOUBLE NOT NULL,
    auto_adjust_quantity BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (maintenance_id, category_id, product_id),
    FOREIGN KEY (maintenance_id) REFERENCES maintenance_guides(maintenance_id),
    FOREIGN KEY (category_id) REFERENCES product_categories(category_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
