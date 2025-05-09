package example.maintenance.estimate.customer.entity;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Customer {

  private int customerId;
  private String lastName;
  private String firstName;
  private String lastNameKana;
  private String firstNameKana;
  private String email;
  private String phoneNumber;


  public Customer() {
  }

  public Customer(int customerId, String lastName, String firstName, String lastNameKana,
      String firstNameKana, String email, String phoneNumber) {
    this.customerId = customerId;
    this.lastName = lastName;
    this.firstName = firstName;
    this.lastNameKana = lastNameKana;
    this.firstNameKana = firstNameKana;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public Customer(String lastName, String firstName, String lastNameKana, String firstNameKana,
      String email, String phoneNumber) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.lastNameKana = lastNameKana;
    this.firstNameKana = firstNameKana;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return customerId == customer.customerId && Objects.equals(lastName, customer.lastName)
        && Objects.equals(firstName, customer.firstName) && Objects.equals(
        lastNameKana, customer.lastNameKana) && Objects.equals(firstNameKana,
        customer.firstNameKana) && Objects.equals(email, customer.email)
        && Objects.equals(phoneNumber, customer.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, lastName, firstName, lastNameKana, firstNameKana, email,
        phoneNumber);
  }
}
