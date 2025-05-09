package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.CustomerInformationDto;
import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.Vehicle;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInformationConverter {

  public static CustomerInformationDto convertToCustomerInformationDto(Customer customer,
      CustomerAddress customerAddress, List<Vehicle> vehicles) {
    CustomerInformationDto customerInformationDto = new CustomerInformationDto();
    customerInformationDto.setCustomer(customer);
    customerInformationDto.setCustomerAddress(customerAddress);
    customerInformationDto.setVehicles(vehicles);
    return customerInformationDto;
  }
}
