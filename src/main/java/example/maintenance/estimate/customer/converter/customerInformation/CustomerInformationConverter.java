package example.maintenance.estimate.customer.converter.customerInformation;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerInformationDto;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;
import example.maintenance.estimate.customer.entity.customerInformation.CustomerAddress;
import example.maintenance.estimate.customer.entity.customerInformation.Vehicle;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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

  public static List<CustomerInformationDto> convertToCustomerInformationDtoList(
      List<Customer> customers,
      Function<Integer, CustomerAddress> addressFetcher,
      Function<Integer, List<Vehicle>> vehicleFetcher
  ) {
    return customers.stream()
        .map(customer -> {
          CustomerAddress address = addressFetcher.apply(customer.getCustomerId());
          List<Vehicle> vehicles = vehicleFetcher.apply(customer.getCustomerId());
          return convertToCustomerInformationDto(customer, address, vehicles);
        })
        .collect(Collectors.toList());
  }
}
