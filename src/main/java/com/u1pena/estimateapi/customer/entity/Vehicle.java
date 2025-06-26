package com.u1pena.estimateapi.customer.entity;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Tag(name = "Vehicle", description = "車両エンティティ")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Vehicle {

  @Schema(description = "車両ID", example = "1")
  private int vehicleId;

  @Schema(description = "顧客ID", example = "1")
  private int customerId;

  @Schema(description = "ナンバープレートの地域", example = "品川")
  private PlateRegion plateRegion;

  @Schema(description = "ナンバープレートの分類番号", example = "500")
  private String plateCategoryNumber;

  @Schema(description = "ナンバープレートのひらがな", example = "あ")
  private String plateHiragana;

  @Schema(description = "ナンバープレートの車両番号", example = "1234")
  private String plateVehicleNumber;

  @Schema(description = "車両のメーカー", example = "toyota")
  private String make;

  @Schema(description = "車両の車体番号", example = "NZE141")
  private String model;

  @Schema(description = "車両の型式", example = "1NZ-FE")
  private String type;

  @Schema(description = "車両の年式", example = "2020-01")
  private YearMonth year;

  @Schema(description = "車両の車検満了日", example = "2024-12-31")
  private LocalDate inspectionDate;

  @Schema(description = "車両のアクティブ状態、廃車時は非アクティブに更新", example = "true")
  private boolean active;

  public Vehicle(int vehicleId, int customerId, PlateRegion plateRegion, String plateCategoryNumber,
      String plateHiragana, String plateVehicleNumber, String make, String model, String type,
      YearMonth year, LocalDate inspectionDate, boolean active) {
    this.vehicleId = vehicleId;
    this.customerId = customerId;
    this.plateRegion = plateRegion;
    this.plateCategoryNumber = plateCategoryNumber;
    this.plateHiragana = plateHiragana;
    this.plateVehicleNumber = plateVehicleNumber;
    this.make = make;
    this.model = model;
    this.type = type;
    this.year = year;
    this.inspectionDate = inspectionDate;
    this.active = active;
  }

  public Vehicle(int customerId, PlateRegion plateRegion, String plateCategoryNumber,
      String plateHiragana, String plateVehicleNumber, String make, String model, String type,
      YearMonth year, LocalDate inspectionDate, boolean active) {
    this.customerId = customerId;
    this.plateRegion = plateRegion;
    this.plateCategoryNumber = plateCategoryNumber;
    this.plateHiragana = plateHiragana;
    this.plateVehicleNumber = plateVehicleNumber;
    this.make = make;
    this.model = model;
    this.type = type;
    this.year = year;
    this.inspectionDate = inspectionDate;
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Vehicle vehicle)) {
      return false;
    }
    return customerId == vehicle.customerId
        && Objects.equals(plateRegion, vehicle.plateRegion)
        && Objects.equals(plateCategoryNumber, vehicle.plateCategoryNumber)
        && Objects.equals(plateHiragana, vehicle.plateHiragana)
        && Objects.equals(plateVehicleNumber, vehicle.plateVehicleNumber)
        && Objects.equals(make, vehicle.make)
        && Objects.equals(model, vehicle.model)
        && Objects.equals(type, vehicle.type)
        && Objects.equals(year, vehicle.year)
        && Objects.equals(inspectionDate, vehicle.inspectionDate)
        && Objects.equals(active, vehicle.active);

  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, plateRegion, plateCategoryNumber, plateHiragana,
        plateVehicleNumber, make, model, type, year, inspectionDate, active);
  }
}
