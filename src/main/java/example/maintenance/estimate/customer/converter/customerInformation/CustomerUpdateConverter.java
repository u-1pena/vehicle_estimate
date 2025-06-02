package example.maintenance.estimate.customer.converter.customerInformation;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerUpdateRequest;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;

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
