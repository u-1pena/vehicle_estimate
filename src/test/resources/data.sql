INSERT INTO customers VALUES (1, 'suzuki', 'ichiro', 'スズキ','イチロウ', 'ichiro@example.ne.jp', '090-1234-5678');

INSERT INTO customers VALUES (2, 'sato', 'hanako', 'サトウ', 'ハナコ','hanako@example.ne.jp', '080-1234-5678');

INSERT INTO customer_addresses VALUES (1, 1, '123-4567', '東京都', '港区', '六本木1-1-1', '都心ビル101');

INSERT INTO vehicles VALUES (1, 1, '品川', '123', 'あ', '1234', 'toyota', 'NZE141-123456', '1AZ-FE', '2020-12', '2027-12-31',true);

INSERT INTO vehicles VALUES (2, 2, '練馬', '456', 'い', '4567', 'nissan', 'ABC123-456789', '1AB-CD', '2022-03', '2028-11-30',true);

INSERT INTO vehicles VALUES (3, 2, '渋谷', '789', 'う', '7890','honda', 'DEF456-789012', '1EF-GH','2021-06', '2029-10-31',true);

INSERT INTO maintenance_info VALUES (1, 1,'カローラアクシオ', '0w-20', 3.7, 3.4, '90915-10003', 'M');

INSERT INTO product_category VALUES (1, 'motor_oil');

INSERT INTO products VALUES (1, 1, 'high_grade_Oil 0W-20', '化学合成油', 2800.0);

INSERT INTO estimate_base VALUES (1, 1);

INSERT INTO estimate_product VALUES (1, 1, 3.4);
