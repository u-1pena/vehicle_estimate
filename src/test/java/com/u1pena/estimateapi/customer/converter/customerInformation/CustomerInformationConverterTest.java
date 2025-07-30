package com.u1pena.estimateapi.customer.converter.customerInformation;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.customer.converter.CustomerInformationConverter;
import com.u1pena.estimateapi.customer.dto.request.CustomerInformationRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerInformationConverterTest {

  CustomerTestHelper customerTestHelper = new CustomerTestHelper();

  @Test
  void 顧客情報と住所と車両情報を１つにまとめることができる() {
    Customer customer = customerTestHelper.customerMock().get(0);
    CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(0);
    List<Vehicle> vehicles = List.of(customerTestHelper.vehicleMock().get(0));

    CustomerInformationRequest actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    Assertions.assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    Assertions.assertThat(actual.getVehicles()).isEqualTo(vehicles);
  }

  @Test
  void 車両情報が複数ある場合に正常に変換できる() {
    Customer customer = customerTestHelper.customerMock().get(1);
    CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(1);
    List<Vehicle> vehicles = customerTestHelper.vehicleMock()
        .stream()
        .filter(vehicle -> vehicle.getCustomerId() == 2)
        .toList();

    CustomerInformationRequest actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    Assertions.assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    Assertions.assertThat(actual.getVehicles()).isEqualTo(vehicles);
    assertThat(actual.getVehicles().size()).isEqualTo(2);
  }

  @Test
  void ユーザーの車両情報が空の場合にも正常に変換できる() {
    Customer customer = customerTestHelper.customerMock().get(2);
    CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(2);
    List<Vehicle> vehicles = emptyList();

    CustomerInformationRequest actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    assertThat(actual.getVehicles()).isEqualTo(vehicles);
    assertThat(actual.getVehicles().size()).isEqualTo(0);
  }
}
