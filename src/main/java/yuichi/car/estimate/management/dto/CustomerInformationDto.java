package yuichi.car.estimate.management.dto;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.Vehicle;

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
