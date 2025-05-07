package yuichi.car.estimate.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import yuichi.car.estimate.management.dto.request.CustomerUpdateRequest;
import yuichi.car.estimate.management.entity.Customer;

class CustomerUpdateConverterTest {

  @Test
  void customerUpdateRequestをcustmerに変換できること() {
    // 準備
    Customer customer = new Customer();
    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
    customerUpdateRequest.setLastName("test");
    customerUpdateRequest.setFirstName("example");
    customerUpdateRequest.setLastNameKana("テスト");
    customerUpdateRequest.setFirstNameKana("サンプル");
    customerUpdateRequest.setEmail("test@example.ne.jp");
    customerUpdateRequest.setPhoneNumber("090-1234-5678");

    // 実行
    CustomerUpdateConverter.customerUpdateConvertToEntity(customer, customerUpdateRequest);

    // 検証
    assertThat(customer.getLastName()).isEqualTo("test");
    assertThat(customer.getFirstName()).isEqualTo("example");
    assertThat(customer.getLastNameKana()).isEqualTo("テスト");
    assertThat(customer.getFirstNameKana()).isEqualTo("サンプル");
    assertThat(customer.getEmail()).isEqualTo("test@example.ne.jp");
    assertThat(customer.getPhoneNumber()).isEqualTo("090-1234-5678");
  }
}
