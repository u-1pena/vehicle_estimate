package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.customer.entity.Customer;
import org.junit.jupiter.api.Test;

class EstimateCustomerConverterTest {

  @Test
  void Customerを見積もり用に変換できる() {
    Customer customer = new Customer();
    customer.setFirstName("tarou");
    customer.setLastName("yamada");
    customer.setFirstNameKana("ﾀﾛｳ");
    customer.setLastNameKana("ﾔﾏﾀﾞ");
    customer.setEmail("tarou@example.com");
    customer.setPhoneNumber("090-1234-5678");

    assertThat(EstimateCustomerConverter.toDto(customer).getFullName())
        .isEqualTo("yamada tarou");
    assertThat(EstimateCustomerConverter.toDto(customer).getFullNameKana())
        .isEqualTo("ﾔﾏﾀﾞ ﾀﾛｳ");
    assertThat(EstimateCustomerConverter.toDto(customer).getEmail())
        .isEqualTo("tarou@example.com");
    assertThat(EstimateCustomerConverter.toDto(customer).getPhoneNumber())
        .isEqualTo("090-1234-5678");
  }
}
