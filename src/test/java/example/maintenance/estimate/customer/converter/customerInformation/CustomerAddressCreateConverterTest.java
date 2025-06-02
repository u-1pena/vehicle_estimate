package example.maintenance.estimate.customer.converter.customerInformation;

import static org.assertj.core.api.Assertions.assertThat;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerAddressCreateRequest;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;
import example.maintenance.estimate.customer.entity.customerInformation.CustomerAddress;
import example.maintenance.estimate.customer.entity.enums.Prefecture;
import org.junit.jupiter.api.Test;

class CustomerAddressCreateConverterTest {

  @Test
  void CustomerAddressCreateRequestからCustomerAddressに変換できること() {
    // 準備
    Customer customer = new Customer();
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest();
    customerAddressCreateRequest.setPostalCode("123-4567");
    customerAddressCreateRequest.setPrefecture("東京都");
    customerAddressCreateRequest.setCity("渋谷区");
    customerAddressCreateRequest.setTownAndNumber("渋谷1-2-3");
    customerAddressCreateRequest.setBuildingNameAndRoomNumber("渋谷ビル101");

    // 実行
    CustomerAddress actual = CustomerAddressCreateConverter.customerAddressConvertToEntity(customer,
        customerAddressCreateRequest);
    // 検証
    assertThat(actual.getCustomerId()).isEqualTo(customer.getCustomerId());
    assertThat(actual.getPostalCode()).isEqualTo("123-4567");
    assertThat(actual.getPrefecture()).isEqualTo(Prefecture.東京都);
    assertThat(actual.getCity()).isEqualTo("渋谷区");
    assertThat(actual.getTownAndNumber()).isEqualTo("渋谷1-2-3");
    assertThat(actual.getBuildingNameAndRoomNumber()).isEqualTo("渋谷ビル101");
  }
}
