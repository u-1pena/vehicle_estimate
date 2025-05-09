package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.request.CustomerCreateRequest;
import example.maintenance.estimate.customer.entity.Customer;

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
