package example.maintenance.estimate.customer.dto;

import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.Vehicle;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInformationDto {

  private Customer customer;
  private CustomerAddress customerAddress;
  private List<Vehicle> vehicles;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomerInformationDto that)) {
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
