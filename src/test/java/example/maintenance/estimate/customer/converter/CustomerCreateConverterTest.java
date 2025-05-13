package example.maintenance.estimate.customer.converter;

import static org.assertj.core.api.Assertions.assertThat;

import example.maintenance.estimate.customer.dto.request.CustomerCreateRequest;
import example.maintenance.estimate.customer.entity.Customer;
import org.junit.jupiter.api.Test;

class CustomerCreateConverterTest {


  @Test
  void CustomerCreateRequestをCustomerに変換できること() {
    // 準備
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest();
    customerCreateRequest.setLastName("test");
    customerCreateRequest.setFirstName("example");
    customerCreateRequest.setLastNameKana("テスト");
    customerCreateRequest.setFirstNameKana("サンプル");
    customerCreateRequest.setEmail("test@example.co.jp");
    customerCreateRequest.setPhoneNumber("090-1234-5678");
    // 実行
    Customer actual = CustomerCreateConverter.customerConvertToEntity(customerCreateRequest);
    // 検証
    assertThat(actual.getLastName()).isEqualTo("test");
    assertThat(actual.getFirstName()).isEqualTo("example");
    assertThat(actual.getLastNameKana()).isEqualTo("テスト");
    assertThat(actual.getFirstNameKana()).isEqualTo("サンプル");
    assertThat(actual.getEmail()).isEqualTo("test@example.co.jp");
    assertThat(actual.getPhoneNumber()).isEqualTo("090-1234-5678");
  }
}
