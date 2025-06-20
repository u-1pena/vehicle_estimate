package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.customer.dto.request.CustomerInformationRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInformationConverter {

  public static CustomerInformationRequest convertToCustomerInformationDto(Customer customer,
      CustomerAddress customerAddress, List<Vehicle> vehicles) {
    CustomerInformationRequest customerInformationRequest = new CustomerInformationRequest();
    customerInformationRequest.setCustomer(customer);
    customerInformationRequest.setCustomerAddress(customerAddress);
    customerInformationRequest.setVehicles(vehicles);
    return customerInformationRequest;
  }

  public static List<CustomerInformationRequest> convertToCustomerInformationDtoList(
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
