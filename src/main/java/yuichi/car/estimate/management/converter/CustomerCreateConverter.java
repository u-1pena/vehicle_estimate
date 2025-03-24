package yuichi.car.estimate.management.converter;

import yuichi.car.estimate.management.dto.request.CustomerCreateRequest;
import yuichi.car.estimate.management.entity.Customer;

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
