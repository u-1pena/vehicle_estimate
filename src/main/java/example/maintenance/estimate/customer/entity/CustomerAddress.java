package example.maintenance.estimate.customer.entity;

import example.maintenance.estimate.customer.entity.enums.Prefecture;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerAddress {

  private int addressId;
  private int customerId;
  private String postalCode;
  private Prefecture prefecture;
  private String city;
  private String townAndNumber;
  private String buildingNameAndRoomNumber;

  public CustomerAddress(int addressId, int customerId, String postalCode, Prefecture prefecture,
      String city,
      String townAndNumber, String buildingNameAndRoomNumber) {
    this.addressId = addressId;
    this.customerId = customerId;
    this.postalCode = postalCode;
    this.prefecture = prefecture;
    this.city = city;
    this.townAndNumber = townAndNumber;
    this.buildingNameAndRoomNumber = buildingNameAndRoomNumber;
  }

  //addressIdを除いたコンストラクタ
  public CustomerAddress(int customerId, String postalCode, Prefecture prefecture, String city,
      String townAndNumber, String buildingNameAndRoomNumber) {
    this.customerId = customerId;
    this.postalCode = postalCode;
    this.prefecture = prefecture;
    this.city = city;
    this.townAndNumber = townAndNumber;
    this.buildingNameAndRoomNumber = buildingNameAndRoomNumber;
  }

  //引数なしのコンストラクタ
  public CustomerAddress() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomerAddress that)) {
      return false;
    }
    return customerId == that.customerId && Objects.equals(postalCode, that.postalCode)
        && prefecture == that.prefecture && Objects.equals(city, that.city)
        && Objects.equals(townAndNumber, that.townAndNumber) && Objects.equals(
        buildingNameAndRoomNumber, that.buildingNameAndRoomNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, postalCode, prefecture, city, townAndNumber,
        buildingNameAndRoomNumber);
  }
}
