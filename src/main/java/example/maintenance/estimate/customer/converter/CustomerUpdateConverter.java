package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.request.CustomerUpdateRequest;
import example.maintenance.estimate.customer.entity.Customer;

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
