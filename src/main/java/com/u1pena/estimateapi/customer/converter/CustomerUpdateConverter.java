package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.customer.dto.request.CustomerUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;

public class CustomerUpdateConverter {

  public static void customerUpdateConvertToEntity(Customer customer,
      CustomerUpdateRequest customerUpdateRequest) {
    customer.setLastName(customerUpdateRequest.getLastName());
    customer.setFirstName(customerUpdateRequest.getFirstName());
    customer.setLastNameKana(customerUpdateRequest.getLastNameKana());
    customer.setFirstNameKana(customerUpdateRequest.getFirstNameKana());
    customer.setEmail(customerUpdateRequest.getEmail());
    customer.setPhoneNumber(customerUpdateRequest.getPhoneNumber());
  }
}
