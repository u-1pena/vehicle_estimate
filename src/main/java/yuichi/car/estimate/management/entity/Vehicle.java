package yuichi.car.estimate.management.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yuichi.car.estimate.management.entity.enums.PlateRegion;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Vehicle {

  private int vehicleId;
  private int customerId;
  private PlateRegion plateRegion;
  private String plateCategoryNumber;
  private String plateHiragana;
  private String plateVehicleNumber;
  private String make;
  private String model;
  private String type;
  private YearMonth year;
  private LocalDate inspectionDate;
  private boolean active;

  public Vehicle(int customerId, PlateRegion plateRegion,
      @NotNull @NotBlank @Pattern(regexp = "^\\d{3}$", message = "plateCategoryNumberは半角数字で3文字で入力してください") String plateCategoryNumber,
      @NotNull @NotBlank @Pattern(regexp = "^[\\u3040-\\u309F]+$", message = "plateHiraganaはひらがなで入力してください") String plateHiragana,
      @NotNull @NotBlank(message = "空白は許可されていません") String plateVehicleNumber,
      @Pattern(regexp = "^[a-z]+$", message = "メーカーは半角英小文字で入力してください") String make,
      @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "車体番号は半角英小文字とハイフンで入力してください") String model,
      @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "型式は半角英小文字とハイフンで入力してください") String type,
      YearMonth parse, LocalDate parsed) {
  }

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
