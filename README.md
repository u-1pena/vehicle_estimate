# Car Maintenance Estimate App

## はじめに

私はガソリンスタンドで働いており、現在使用している多くの管理ツールには満足しています。しかし、最近気になる変化があります。それは「新車検証」の普及です。この新車検証にはICチップが内蔵されており、車両情報をデジタル形式で読み取ることが可能です。また、国土交通省が提供する車検証アプリを使えば、スマートフォンを通じて簡単に車両情報を確認できます。
この技術の可能性に着目し、APIを学びながら「車両情報からメンテナンス費用の見積もりを行い、その場で購入ができるシステム」の開発を思いつきました。このシステムを通じて、ガソリンスタンド業界がデジタル管理へと移行し、顧客がより便利に車両の維持管理を行えるようになることを目指しています。

## 国土交通省の車検証アプリについて

車検証アプリから車両情報を取得するAPIとしての利用は、国土交通省への申請が必要ですが、個人としては利用が難しい面があります。ただし、アプリ自体は個人でも利用可能であり、店頭での対面商談時に車検証を登録し、お客様が自宅で利用できるようにすることも一つの手段です。

# 使用技術一覧

![Static Badge](https://img.shields.io/badge/Spring_Boot-3.2.3-%236DB33F?style=plastic&logo=Spring&logoColor=%236DB33F)
![Static Badge](https://img.shields.io/badge/Docker-%232496ED?style=plastic&logo=Docker&logoColor=white)
![Static Badge](https://img.shields.io/badge/MySQL-%234479A1?style=plastic&logo=MySQL&logoColor=white)
![Static Badge](https://img.shields.io/badge/Language-Java-red?style=plastic)
![Static Badge](https://img.shields.io/badge/Git_Hub-%23181717?style=plastic&logo=GitHub&logoColor=white)

<br>
<br>

# サービス概要

### ユーザーが利用できること

* ユーザーがユーザー情報を登録し、車検証アプリや車両情報を直接入力してユーザー情報と車両情報を登録します。
* 登録した車両情報をもとに車両に必要なメンテンナンスの情報が呼び出され登録されます。
* 見積もりを作成時には、メンテナンス情報から適合のグレードや数量が自動で抽出されるので車両に適した商品で簡単に見積もりを作成することができます。
* クレジット情報を登録することでその場で決済することができます。また、店頭での点検にて改めて購入したい場合は店頭支払いも可能です。
* 作成した見積もりはDBに保存されあとで見積もり情報を作成日時で取得し、確認することができます。

### 管理者が利用できること

* メンテナンス情報を追加登録・更新・削除ができます。
* 新しくメンテナンス情報を追加登録できます。
* 各メンテナンス商品・商品カテゴリーを追加登録や修正・削除ができます。

<br>
<br>

# 補足

* 車両情報：車の年式・型式・メーカーなど
* メンテナンス情報：オイル交換時のオイルの粘度や数量、交換部品の詳細
* メンテナンス商品：エンジンオイルやオイルフィルターなど、洗車も含む

<br>
<br>

# API設計とエンドポイントについて

## ユーザーがアクセスできるエンドポイント

| method |    end_point     |    description    |
|:------:|:----------------:|:-----------------:|
|  POST  |      /users      |    ユーザー情報を登録する    |
|  GET   |      /users      |    ユーザー情報を取得する    |
|  PUT   |      /users      |    ユーザー情報を更新する    |
| DELETE |      /users      |    ユーザー情報を削除する    |
|  POST  |     /vehicle     |     車両情報を登録する     |
|  GET   |     /vehicle     |     車両情報を取得する     |
| DELETE |     /vehicle     |     車両情報を削除する     |
|  POST  |    /estimate     |  メンテナンス見積もりを登録する  |
|  GET   |    /estimate     | メンテナンス見積もり情報を取得する |
|  PUT   |    /estimate     | メンテナンス見積もり情報を修正する |
| DELETE |    /estimate     | メンテナンス見積もり情報を削除する |
|  POST  |     /payment     |    支払い情報を登録する     |
|  GET   |     /payment     |    支払い情報を取得する     |
| DELETE |     /payment     |    支払い情報を削除する     |
|  POST  | /payment/confirm |       購入する        |

上記のエンドポイントはAdminユーザーもすべてアクセスできる。

<br>
<br>

## Adminユーザーのみがアクセスできるエンドポイント

| method |     end_point     |     description      |
|:------:|:-----------------:|:--------------------:|
|  POST  |   /admin/login    |    Adminユーザーのログイン    |
|  POST  |   /maintenance    |   車両メンテナンス情報を登録する    |
|  PUT   |   /maintenance    |   車両メンテナンス情報を更新する    |
| DELETE |   /maintenance    |   車両メンテナンス情報を削除する    |
|  POST  |     /product      |    メンテナンス商品を登録する　    |
|  PUT   |     /product      |   メンテナンス商品情報を更新する    |
| DELETE |     /product      |   メンテナンス商品情報を削除する    |
|  GET   |     /product      |   メンテナンス商品情報を検索する    |
|  POST  | /product/category |  メンテナンス商品カテゴリーを登録する  |
|  PUT   | /product/category | メンテナンス商品カテゴリー情報を更新する |
| DELETE | /product/category | メンテナンス商品カテゴリー情報を削除する |
|  GET   | /product/category | メンテナンス商品カテゴリー情報を検索する |

<br>
<br>

# 登録から支払いまでのフロー

## ユーザーが見積もりから購入するまでのフローですが、ユーザー側もログイン機能を作成するか検討中

```mermaid

---
config:
  look: handDrawn
  layout: elk
  theme: base
---
flowchart TD
A(["START"]) --> B(["ユーザー登録されている"])
B -- yes --> C(["車両情報が登録されている"])
C -- yes --> E(["見積もりを作成する"])
C -- No --> G(["車両情報を登録する"])
B -- No --> D(["ユーザーを登録する"])
D ---> C
G ---> E
E --> F(["支払い情報が登録されている"])
F -- Yes -----> H(["決済方法選択"])
F -- No --> I(["支払い情報を登録する"])
I -- Yes ----> H
H ---> K(["店頭決済"]) & J(["クレジット決済"])
I -- No ---> K

```

1. ユーザー登録を行います
2. 車両情報を登録します
3. 希望のメンテナンスを見積もります。
4. その場で購入したい場合は支払い情報を登録します。
5. 店頭決済を希望される場合は支払い情報なしで確定できます。

# ER図

```mermaid
---
config:
  theme: forest
---
erDiagram
    Customer ||--o{ Vehicle: ""
    Customer ||--o{ Payment: ""
    Vehicle ||--|| MaintenanceInfo: ""
    EstimateBase ||--o{ EstimateProduct: ""
    MaintenanceInfo ||--o{ EstimateBase: ""
    EstimateProduct }o--o{ Product: ""
    Product }o--|| ProductCategory: ""
    login {
        int loginId PK
        String Password
    }
    Customer {
        int customerId PK
        String name
        String postalCode
        String address
        String email
        String phone
    }
    Payment {
        int paymentId
        String cardNumber
        String cardBrand
        String cardHolder
        YearMonth ExpirationDate
    }

    Vehicle {
        int VehicleId PK
        int customerId FK
        String make
        String model
        String year
        String type
        LocalDateTime inspectionDueDate
    }
    MaintenanceInfo {
        int maintenanceId PK
        int vehicleId FK
        String oilViscosity
        double OilQuantityWithFilter
        double OilQuantityWithoutFilter
        String oilFilterPartNumber
        String carWashSize
    }
    EstimateBase {
        int estimateId PK
        int maintenanceId FK
    }
    EstimateProduct {
        int estimateId PK
        int productId FK
        double quantity
    }
    Product {
        int productId PK
        String categoryId FK
        String name
        String description
        decimal price
    }
    ProductCategory {
        int categoryId PK
        String name
    }


```

# ガントチャートにてスケジュール化

```mermaid
---
config:
  theme: forest
---

gantt
    title 実装スケジュール
    dateFormat YYYY-MM-DD
    excludes 02-18,02-20

    section 改修
        projectの修正: a2, 02-16, 24h
        users: after a2, 1d
        payments: after a2, 1d

    section vehicle
        READ処理: c1, 02-21, 4d
        CREATE処: after c1, 4d
    section estimate
        READ処理: d1, 03-04, 8d
        CREATE処理: after d1, 8d
    section maintenance
        READ処理: f1, 03-20, 4d
        CREATE処理: after f1, 4d
    section product
        READ処理: h1, 03-28, 4d
        CREATE処: after h1, 4d
    section productCategory
        READ処理: i1, 04-05, 4d
        CREATE処理: after i1, 4d
    section login
        ログイン実装: e1, 04-13, 4d
        CREATE処: after e1, 4d

```
