package com.u1pena.estimateapi.customer.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Tag(name = "Customer", description = "顧客エンティティ")
@Getter
@Setter
@Builder
public class Customer {

  @Schema(description = "顧客ID", example = "1")
  private int customerId;
  @Schema(description = "顧客の姓", example = "tanaka")
  private String lastName;
  @Schema(description = "顧客の名", example = "tarou")
  private String firstName;
  @Schema(description = "顧客の姓（カタカナ）", example = "タナカ")
  private String lastNameKana;
  @Schema(description = "顧客の名（カタカナ）", example = "タロウ")
  private String firstNameKana;
  @Schema(description = "Eメール", example = "tarou@example.com")
  private String email;
  @Schema(description = "電話番号", example = "090-1234-5678")
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
