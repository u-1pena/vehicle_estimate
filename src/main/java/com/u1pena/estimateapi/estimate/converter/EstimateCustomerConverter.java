package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;

public class EstimateCustomerConverter {

  public static CustomerResponse toDto(Customer customer) {
    String fullName = customer.getLastName() + " " + customer.getFirstName();
    String fullNameKana = customer.getLastNameKana() + " " + customer.getFirstNameKana();
    return CustomerResponse.builder()
        .fullName(fullName)
        .fullNameKana(fullNameKana)
        .email(customer.getEmail())
        .phoneNumber(customer.getPhoneNumber())
        .build();
  }
}
