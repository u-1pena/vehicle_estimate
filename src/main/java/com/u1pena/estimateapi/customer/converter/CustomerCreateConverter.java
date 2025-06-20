package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.customer.dto.request.CustomerCreateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;

public class CustomerCreateConverter {

  public static Customer customerConvertToEntity(CustomerCreateRequest customerCreateRequest) {
    return Customer.builder()
        .lastName(customerCreateRequest.getLastName())
        .firstName(customerCreateRequest.getFirstName())
        .lastNameKana(customerCreateRequest.getLastNameKana())
        .firstNameKana(customerCreateRequest.getFirstNameKana())
        .email(customerCreateRequest.getEmail())
        .phoneNumber(customerCreateRequest.getPhoneNumber())
        .build();
  }
}
