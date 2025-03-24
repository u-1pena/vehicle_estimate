package yuichi.car.estimate.management.converter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import yuichi.car.estimate.management.dto.CustomerInformationDto;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.Vehicle;

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
