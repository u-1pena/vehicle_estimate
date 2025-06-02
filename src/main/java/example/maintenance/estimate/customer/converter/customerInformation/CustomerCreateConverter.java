package example.maintenance.estimate.customer.converter.customerInformation;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerCreateRequest;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;

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
