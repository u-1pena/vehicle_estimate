package com.u1pena.estimateapi.customer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import com.u1pena.estimateapi.customer.mapper.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  CustomerTestHelper customerTestHelper;

  @BeforeEach
  void setup() {
    customerTestHelper = new CustomerTestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 指定した顧客idで顧客情報を検索することができる() {
      Optional<Customer> actual = customerRepository.findCustomerByCustomerId(1);
      Customer expected = customerTestHelper.customerMock().get(0);
      assertThat(actual).hasValue(expected);
    }

    @Test
    void 指定した顧客idで顧客住所を検索することができる() {
      Optional<CustomerAddress> actual = customerRepository.findCustomerAddressByCustomerId(1);
      CustomerAddress expected = customerTestHelper.customerAddressMock().get(0);
      assertThat(actual).hasValue(expected);
    }

    @Test
    void 指定した顧客idで車両情報を検索することができる() {
      List<Vehicle> actual = customerRepository.findVehicleByCustomerId(1);
      Vehicle expected = customerTestHelper.vehicleMock().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 指定した顧客idで車両情報が複数あっても検索することができる() {
      List<Vehicle> actual = customerRepository.findVehicleByCustomerId(2);
      List<Vehicle> expected = customerTestHelper.vehicleMock().stream()
          .filter(vehicle -> vehicle.getCustomerId() == 2)
          .toList();
      assertThat(actual).isEqualTo(expected);
      assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void 名前で顧客情報を検索することができる() {
      List<Customer> actual = customerRepository.findCustomerByName("suzuki");
      Customer expected = customerTestHelper.customerMock().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 読みがなで顧客情報を検索することができる() {
      List<Customer> actual = customerRepository.findCustomerByNameKana("スズキ");
      Customer expected = customerTestHelper.customerMock().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 電話番号で顧客情報を検索することができる() {
      Optional<Customer> actual = customerRepository.findCustomerByPhoneNumber("090-1234-5678");
      Customer expected = customerTestHelper.customerMock().get(0);
      assertThat(actual).contains(expected);
    }

    @Test
    void メールアドレスで顧客情報を検索することができる() {
      Optional<Customer> actual = customerRepository.findCustomerByEmail("ichiro@example.com");
      Customer expected = customerTestHelper.customerMock().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual).isPresent();
    }

    @Test
    void 車両番号4桁で顧客情報を検索することができる() {
      List<Vehicle> actual = customerRepository.findVehicleByPlateNumber("1234");
      Vehicle expected = customerTestHelper.vehicleMock().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual).hasSize(1);
    }
  }

  @Nested
  class CreateClass {

    @Test
    void 顧客情報を登録することができる() {
      //準備
      int initialSize = customerRepository.findAllCustomers().size();
      Customer customer = customerTestHelper.customerCreateMock();
      //実行
      customerRepository.createCustomer(customer);
      //検証
      List<Customer> actual = customerRepository.findAllCustomers();
      assertThat(actual).hasSize(initialSize + 1);
    }

    @Test
    void 顧客住所を登録することができる() {
      //準備
      int initialSize = customerRepository.findAllCustomerAddresses().size();
      Customer customer = customerTestHelper.customerCreateMock();
      customerRepository.createCustomer(customer);
      CustomerAddress customerAddress = customerTestHelper.customerAddressCreateMock(customer);
      //実行
      customerRepository.createCustomerAddress(customerAddress);
      //検証
      List<CustomerAddress> actual = customerRepository.findAllCustomerAddresses();
      assertThat(actual).hasSize(initialSize + 1);
    }

    @Test
    void 車両情報を登録することができる() {
      //準備
      int initialSize = customerRepository.findAllVehicles().size();
      Customer customer = customerTestHelper.customerCreateMock();
      customerRepository.createCustomer(customer);
      Vehicle vehicle = customerTestHelper.vehicleCreateMock(customer);
      //実行
      customerRepository.createVehicle(vehicle);
      //検証
      List<Vehicle> actual = customerRepository.findAllVehicles();
      assertThat(actual).hasSize(initialSize + 1);
    }
  }

  @Nested
  class DeleteClass {


    @Test
    void 顧客情報を削除できる() {
      //準備
      int initialSize = customerRepository.findAllCustomers().size();
      //実行
      customerRepository.deleteCustomer(1);
      //検証
      List<Customer> actual = customerRepository.findAllCustomers();
      assertThat(actual).hasSize(initialSize - 1);
    }

    @Test
    void 車両情報を削除できる() {
      //準備
      Optional<Vehicle> before = customerRepository.findVehicleByVehicleId(1);
      assertThat(before).isPresent();
      assertThat(before.get().isActive()).isEqualTo(true);
      //実行
      customerRepository.deactivateVehicle(1);
      //検証
      Optional<Vehicle> actual = customerRepository.findVehicleByVehicleId(1);
      assertThat(actual.get().isActive()).isEqualTo(false);
    }
  }

  @Nested
  class UpdateClass {

    @Test
    void IDで指定した顧客情報が更新できること() {
      //準備
      Customer customer = customerTestHelper.customerMock().get(0);
      customer.setPhoneNumber("090-1111-1111");

      //実行
      customerRepository.updateCustomer(customer);
      //検証
      Optional<Customer> actual = customerRepository.findCustomerByCustomerId(1);
      assertThat(actual).isPresent();
      assertThat(actual.get().getPhoneNumber()).isNotEqualTo("090-1234-5678");
      assertThat(actual.get().getPhoneNumber()).isEqualTo("090-1111-1111");

    }

    @Test
    void IDで指定した顧客住所が更新されること() {
      CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(0);
      customerAddress.setPrefecture(Prefecture.神奈川県);
      customerAddress.setCity("横浜市");
      customerAddress.setTownAndNumber("みなとみらい1-1-1");
      customerAddress.setBuildingNameAndRoomNumber("ランドマークタワー101");

      //実行
      customerRepository.updateCustomerAddress(customerAddress);
      //検証
      Optional<CustomerAddress> actual = customerRepository.findCustomerAddressByCustomerId(1);
      assertThat(actual).isPresent();
      assertThat(actual.get().getPrefecture()).isEqualTo(Prefecture.神奈川県);
      assertThat(actual.get().getCity()).isEqualTo("横浜市");
      assertThat(actual.get().getTownAndNumber()).isEqualTo("みなとみらい1-1-1");
      assertThat(actual.get().getBuildingNameAndRoomNumber()).isEqualTo("ランドマークタワー101");
    }

    @Test
    void IDで更新した車両情報が更新されること() {
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);
      vehicle.setPlateHiragana("か");
      vehicle.setPlateVehicleNumber("1111");

      //実行
      customerRepository.updateVehicle(vehicle);
      //検証
      Optional<Vehicle> actual = customerRepository.findVehicleByVehicleId(1);
      assertThat(actual).isPresent();
      assertThat(actual.get().getPlateHiragana()).isEqualTo("か");
      assertThat(actual.get().getPlateVehicleNumber()).isEqualTo("1111");
    }
  }
}
