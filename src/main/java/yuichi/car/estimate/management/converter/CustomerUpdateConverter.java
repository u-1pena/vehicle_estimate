package yuichi.car.estimate.management.converter;

import yuichi.car.estimate.management.dto.request.CustomerUpdateRequest;
import yuichi.car.estimate.management.entity.Customer;

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
