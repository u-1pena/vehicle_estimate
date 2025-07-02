package com.u1pena.estimateapi.customer.entity;

import com.u1pena.estimateapi.common.enums.Prefecture;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Tag(name = "CustomerAddress", description = "顧客住所エンティティ")
@Getter
@Setter
@Builder
public class CustomerAddress {

  @Schema(description = "住所ID", example = "1")
  private int addressId;

  @Schema(description = "顧客ID、顧客IDと1to1の関係", example = "1")
  private int customerId;

  @Schema(description = "郵便番号", example = "123-4567")
  private String postalCode;

  @Schema(description = "都道府県", example = "東京都")
  private Prefecture prefecture;

  @Schema(description = "市区町村", example = "新宿区")
  private String city;

  @Schema(description = "町名・番地", example = "西新宿2-8-1")
  private String townAndNumber;

  @Schema(description = "建物名・部屋番号　なければブランクでも可", example = "新宿ビル101号室")
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
