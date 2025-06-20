INSERT INTO customers VALUES (1, 'suzuki', 'ichiro', 'スズキ','イチロウ', 'ichiro@example.ne.jp', '090-1234-5678');

INSERT INTO customers VALUES (2, 'sato', 'hanako', 'サトウ', 'ハナコ','hanako@example.ne.jp', '080-1234-5678');

INSERT INTO customer_addresses VALUES (1, 1, '123-4567', '東京都', '港区', '六本木1-1-1', '都心ビル101');

INSERT INTO vehicles VALUES (1, 1, '品川', '123', 'あ', '1234', 'toyota', 'DBA-NZE141', '1NZ', '2010-12', '2027-12-31',true);

INSERT INTO vehicles VALUES (2, 2, '練馬', '456', 'い', '4567', 'nissan', 'ABC123-456789', '1AB-CD', '2022-03', '2028-11-30',true);

INSERT INTO vehicles VALUES (3, 2, '渋谷', '789', 'う', '7890','honda', 'DEF456-789012', '1EF-GH','2021-06', '2029-10-31',true);

INSERT INTO maintenance_guides VALUES (1, 'toyota', 'カローラアクシオ', 'DBA-NZE141', '1NZ', '2006-10', '2012-05', '0w-20', 3.7, 3.4, '90915-10003', 'M');
INSERT INTO maintenance_guides VALUES (2, 'nissan', 'セレナ', 'DAA-GC27', 'MR20DD', '2016-08', '2020-07', '0w-16', 3.8, 3.4, 'AY100-N004', 'LL');
INSERT INTO maintenance_guides VALUES (3, 'honda', 'フィット', 'DBA-GK3', 'L13B', '2013-09', '2020-02', '0w-20', 3.1, 3.3, '15400-RTA-004', 'S');
INSERT INTO maintenance_guides VALUES (4, 'toyota', 'アクア', '6AA-MXPK11', 'M15A-FXE', '2022-11', '9999-12', '0w-8', 3.6, 3.3, '90915-10009', 'S');

INSERT INTO product_categories VALUES (1, 'motor_oil');
INSERT INTO product_categories VALUES (2, 'oil_filter');
INSERT INTO product_categories VALUES (3, 'car_wash');

INSERT INTO products VALUES (1, 1, 'ハイグレードオイル_0w-20', '化学合成油_0w-20', '0w-20', 2800.0);
INSERT INTO products VALUES (2, 1, 'ハイグレードオイル_5w-30', '化学合成油_5w-30', '5w-30', 2800.0);
INSERT INTO products VALUES (3, 1, 'ハイグレードオイル_5w-40', '化学合成油_5w-40', '5w-40', 2800.0);
INSERT INTO products VALUES (4, 1, 'ハイグレードオイル_0w-16', '化学合成油_0w-16', '0w-16', 2800.0);
INSERT INTO products VALUES (5, 1, 'ノーマルグレードオイル_0w-20', '部分合成油_0w-20', '0w-20', 2000.0);
INSERT INTO products VALUES (6, 1, 'ノーマルグレードオイル_5w-30', '部分合成油_5w-30', '5w-30', 2000.0);
INSERT INTO products VALUES (7, 1, 'オイル_5w-30', '鉱物油_5w-30', '5w-30', 1400.0);
INSERT INTO products VALUES (8, 1, 'オイル_10w-30', '鉱物油_10w-30', '10w-30', 1300.0);
INSERT INTO products VALUES (9, 3, '手洗い洗車', '手洗い洗車Sサイズ', 'S', 2200.0);
INSERT INTO products VALUES (10, 2, 'オイルフィルター', 'to-101', '90915-10003', 1000.0);


INSERT INTO estimate_bases VALUES (1, 1, 1, 1, CURRENT_DATE);

INSERT INTO guide_product_permissions VALUES (1, 1, 1, 4.0, true);
