package com.u1pena.estimateapi.customer.helper;

import static com.u1pena.estimateapi.common.enums.PlateRegion.品川;
import static com.u1pena.estimateapi.common.enums.PlateRegion.渋谷;
import static com.u1pena.estimateapi.common.enums.PlateRegion.練馬;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerUpdateRequest;
import com.u1pena.estimateapi.customer.dto.request.VehicleCreateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.master.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

  /*
   * テスト用のユーザーデータを作成するクラス
   */
  public List<Customer> customerMock() {
    return List.of(
        new Customer(1, "suzuki", "ichiro", "スズキ",
            "イチロウ", "ichiro@example.ne.jp", "090-1234-5678"),
        new Customer(2, "sato", "hanako", "サトウ", "ハナコ",
            "hanako@example.ne.jp", "080-1234-5678"),
        new Customer(3, "tanaka", "taro", "タナカ", "タロウ",
            "taro@example.ne.jp", "070-1234-5678"));
  }

  /*
   * テスト用の住所データを作成するクラス
   */
  public List<CustomerAddress> customerAddressMock() {
    return List.of(new CustomerAddress(1, 1, "123-4567", Prefecture.東京都, "港区",
            "六本木1-1-1", "都心ビル101"),
        new CustomerAddress(2, 2, "123-4568", Prefecture.東京都, "港区",
            "六本木1-2-3", "副都心ビル302"),
        new CustomerAddress(3, 3, "123-4569", Prefecture.東京都, "港区",
            "六本木1-3-4", "中央ビル203"));
  }

  /*
   * テスト用の車両データを作成するクラス
   */
  public List<Vehicle> vehicleMock() {
    return new ArrayList<>(List.of(
        new Vehicle(1, 1, 品川, "123",
            "あ", "1234", "toyota", "DBA-NZE141",
            "1NZ", YearMonth.of(2010, 12), LocalDate.of(2027, 12, 31), true),
        new Vehicle(2, 2, 練馬, "456",
            "い", "4567", "nissan", "ABC123-456789",
            "1AB-CD", YearMonth.of(2022, 3), LocalDate.of(2028, 11, 30), true),
        new Vehicle(3, 2, 渋谷, "789", "う",
            "7890", "honda", "DEF456-789012",
            "1EF-GH", YearMonth.of(2021, 6), LocalDate.of(2029, 10, 31), true)));
  }

  public Customer customerCreateMock() {
    Customer customer = new Customer(
        customerCreateRequestMock().getLastName(),
        customerCreateRequestMock().getFirstName(),
        customerCreateRequestMock().getLastNameKana(),
        customerCreateRequestMock().getFirstNameKana(),
        customerCreateRequestMock().getEmail(),
        customerCreateRequestMock().getPhoneNumber());
    return customer;
  }

  public CustomerCreateRequest customerCreateRequestMock() {
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest();
    customerCreateRequest.setLastName("yamada");
    customerCreateRequest.setFirstName("hanako");
    customerCreateRequest.setLastNameKana("ヤマダ");
    customerCreateRequest.setFirstNameKana("ハナコ");
    customerCreateRequest.setEmail("hanako@example.com");
    customerCreateRequest.setPhoneNumber("090-1111-9999");
    return customerCreateRequest;
  }

  public CustomerAddress customerAddressCreateMock(Customer customer) {
    CustomerAddress customerAddress = new CustomerAddress(
        customer.getCustomerId(),
        customerAddressCreateRequestMock().getPostalCode(),
        Prefecture.valueOf(customerAddressCreateRequestMock().getPrefecture()),
        customerAddressCreateRequestMock().getCity(),
        customerAddressCreateRequestMock().getTownAndNumber(),
        customerAddressCreateRequestMock().getBuildingNameAndRoomNumber());
    return customerAddress;
  }

  public CustomerAddressCreateRequest customerAddressCreateRequestMock() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest();
    customerAddressCreateRequest.setPostalCode("123-4567");
    customerAddressCreateRequest.setPrefecture("東京都");
    customerAddressCreateRequest.setCity("港区");
    customerAddressCreateRequest.setTownAndNumber("六本木1-1-1");
    customerAddressCreateRequest.setBuildingNameAndRoomNumber("都心ビル101");
    return customerAddressCreateRequest;
  }

  public Vehicle vehicleCreateMock(Customer customer) {
    Vehicle vehicle = new Vehicle(
        customer.getCustomerId(),
        PlateRegion.valueOf(vehicleCreateRequestMock().getPlateRegion()),
        vehicleCreateRequestMock().getPlateCategoryNumber(),
        vehicleCreateRequestMock().getPlateHiragana(),
        vehicleCreateRequestMock().getPlateVehicleNumber(),
        vehicleCreateRequestMock().getMake(),
        vehicleCreateRequestMock().getModel(),
        vehicleCreateRequestMock().getType(),
        YearMonth.parse(vehicleCreateRequestMock().getYear()),
        LocalDate.parse(vehicleCreateRequestMock().getInspectionDate()),
        true);
    return vehicle;
  }

  public VehicleCreateRequest vehicleCreateRequestMock() {
    VehicleCreateRequest vehicleCreateRequest = new VehicleCreateRequest();
    vehicleCreateRequest.setPlateRegion("品川");
    vehicleCreateRequest.setPlateCategoryNumber("123");
    vehicleCreateRequest.setPlateHiragana("あ");
    vehicleCreateRequest.setPlateVehicleNumber("1234");
    vehicleCreateRequest.setMake("toyota");
    vehicleCreateRequest.setModel("DBA-NZE141");
    vehicleCreateRequest.setType("1NZ");
    vehicleCreateRequest.setYear("2010-12");
    vehicleCreateRequest.setInspectionDate("2027-12-31");
    return vehicleCreateRequest;
  }

  public Customer customerUpdateMock(Customer customer) {
    Customer customer1 = new Customer(
        customerUpdateRequestMock().getLastName(),
        customerUpdateRequestMock().getFirstName(),
        customerUpdateRequestMock().getLastNameKana(),
        customerUpdateRequestMock().getFirstNameKana(),
        customerUpdateRequestMock().getEmail(),
        customerUpdateRequestMock().getPhoneNumber());
    return customer1;
  }

  public CustomerUpdateRequest customerUpdateRequestMock() {
    CustomerUpdateRequest customerUpdateRequestMock = new CustomerUpdateRequest();
    customerUpdateRequestMock.setLastName("yamada");
    customerUpdateRequestMock.setFirstName("hanako");
    customerUpdateRequestMock.setLastNameKana("ヤマダ");
    customerUpdateRequestMock.setFirstNameKana("ハナコ");
    customerUpdateRequestMock.setEmail("hanako@example.com");
    customerUpdateRequestMock.setPhoneNumber("090-1111-9999");
    return customerUpdateRequestMock;
  }

  public List<Product> productMock() {
    return List.of(
        new Product(
            1, 1, "ハイグレードオイル_0w-20",
            "化学合成油_0w-20", "0w-20", BigDecimal.valueOf(2800.0)),
        new Product(
            2, 1, "ハイグレードオイル_5w-30",
            "化学合成油_5w-30", "5w-30", BigDecimal.valueOf(2800.0)),
        new Product(
            3, 1, "ハイグレードオイル_5w-40",
            "化学合成油_5w-40", "5w-40", BigDecimal.valueOf(2800.0)),
        new Product(
            4, 1, "ハイグレードオイル_0w-16",
            "化学合成油_0w-16", "0w-16", BigDecimal.valueOf(2800.0)));

    //INSERT INTO products VALUES (5, 1, 'ノーマルグレードオイル_0w-20', '部分合成油_0w-20', '0w-20', 2000.0);
    //INSERT INTO products VALUES (6, 1, 'ノーマルグレードオイル_5w-30', '部分合成油_5w-30', '5w-30', 2000.0);
    //INSERT INTO products VALUES (7, 1, 'オイル_5w-30', '鉱物油_5w-30', '5w-30', 1400.0);
    //INSERT INTO products VALUES (8, 1, 'オイル_10w-30', '鉱物油_10w-30', '10w-30', 1300.0);
  }
}
