package com.u1pena.estimateapi.customer.dto.request;

import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInformationRequest {

  private Customer customer;
  private CustomerAddress customerAddress;
  private List<Vehicle> vehicles;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomerInformationRequest that)) {
      return false;
    }
    return Objects.equals(customer, that.customer) && Objects.equals(
        customerAddress, that.customerAddress) && Objects.equals(vehicles, that.vehicles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, customerAddress, vehicles);
  }
}
