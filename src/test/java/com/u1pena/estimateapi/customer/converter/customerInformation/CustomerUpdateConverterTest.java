package com.u1pena.estimateapi.customer.converter.customerInformation;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.customer.converter.CustomerUpdateConverter;
import com.u1pena.estimateapi.customer.dto.request.CustomerUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import org.junit.jupiter.api.Test;

class CustomerUpdateConverterTest {

  @Test
  void customerUpdateRequestをCustomerに変換できること() {
    // 準備
    Customer customer = new Customer();
    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
    customerUpdateRequest.setLastName("test");
    customerUpdateRequest.setFirstName("com");
    customerUpdateRequest.setLastNameKana("テスト");
    customerUpdateRequest.setFirstNameKana("サンプル");
    customerUpdateRequest.setEmail("test@example.ne.jp");
    customerUpdateRequest.setPhoneNumber("090-1234-5678");

    // 実行
    CustomerUpdateConverter.customerUpdateConvertToEntity(customer, customerUpdateRequest);

    // 検証
    assertThat(customer.getLastName()).isEqualTo("test");
    assertThat(customer.getFirstName()).isEqualTo("com");
    assertThat(customer.getLastNameKana()).isEqualTo("テスト");
    assertThat(customer.getFirstNameKana()).isEqualTo("サンプル");
    assertThat(customer.getEmail()).isEqualTo("test@example.ne.jp");
    assertThat(customer.getPhoneNumber()).isEqualTo("090-1234-5678");
  }
}
