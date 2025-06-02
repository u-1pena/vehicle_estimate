package example.maintenance.estimate.customer.converter.customerInformation;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerInformationDto;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;
import example.maintenance.estimate.customer.entity.customerInformation.CustomerAddress;
import example.maintenance.estimate.customer.entity.customerInformation.Vehicle;
import example.maintenance.estimate.customer.helper.TestHelper;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerInformationConverterTest {

  TestHelper testHelper = new TestHelper();

  @Test
  void 顧客情報と住所と車両情報を１つにまとめることができる() {
    Customer customer = testHelper.customerMock().get(0);
    CustomerAddress customerAddress = testHelper.customerAddressMock().get(0);
    List<Vehicle> vehicles = List.of(testHelper.vehicleMock().get(0));

    CustomerInformationDto actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    Assertions.assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    Assertions.assertThat(actual.getVehicles()).isEqualTo(vehicles);
  }

  @Test
  void 車両情報が複数ある場合に正常に変換できる() {
    Customer customer = testHelper.customerMock().get(1);
    CustomerAddress customerAddress = testHelper.customerAddressMock().get(1);
    List<Vehicle> vehicles = testHelper.vehicleMock()
        .stream()
        .filter(vehicle -> vehicle.getCustomerId() == 2)
        .toList();

    CustomerInformationDto actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    Assertions.assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    Assertions.assertThat(actual.getVehicles()).isEqualTo(vehicles);
    assertThat(actual.getVehicles().size()).isEqualTo(2);
  }

  @Test
  void ユーザーの車両情報が空の場合にも正常に変換できる() {
    Customer customer = testHelper.customerMock().get(2);
    CustomerAddress customerAddress = testHelper.customerAddressMock().get(2);
    List<Vehicle> vehicles = emptyList();

    CustomerInformationDto actual = CustomerInformationConverter.convertToCustomerInformationDto(
        customer,
        customerAddress, vehicles);

    assertThat(actual.getCustomer()).isEqualTo(customer);
    assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    assertThat(actual.getVehicles()).isEqualTo(vehicles);
    assertThat(actual.getVehicles().size()).isEqualTo(0);
  }
}
