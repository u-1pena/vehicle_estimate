package com.u1pena.estimateapi.master.entity;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class MaintenanceGuide {

  @Schema(description = "メンテナンスID", example = "1")
  private int maintenanceId;
  @Schema(description = "車両メーカー", example = "toyota")
  private String make;
  @Schema(description = "車両名", example = "プリウス")
  private String vehicleName;
  @Schema(description = "車両型式", example = "ZVW30")
  private String model;
  @Schema(description = "エンジン型式", example = "2ZR-FXE")
  private String type;
  @Schema(description = "車両年式開始", example = "2010-01")
  private YearMonth startYear;
  @Schema(description = "車両年式終了", example = "2015-12")
  private YearMonth endYear;
  @Schema(description = "オイル粘度", example = "0w-20")
  private String oilViscosity;
  @Schema(description = "オイル量（フィルターあり）", example = "3.5")
  private double oilQuantityWithFilter;
  @Schema(description = "オイル量（フィルターなし）", example = "3.0")
  private double oilQuantityWithoutFilter;
  @Schema(description = "純正オイルフィルター型番", example = "12345-67890")
  private String oilFilterPartNumber;
  @Schema(description = "洗車サイズ", example = "M")
  private CarWashSize carWashSize;

  public MaintenanceGuide(int maintenanceId, String make, String vehicleName, String model,
      String type, YearMonth startYear, YearMonth endYear, String oilViscosity,
      double oilQuantityWithFilter, double oilQuantityWithoutFilter, String oilFilterPartNumber,
      CarWashSize carWashSize) {
    this.maintenanceId = maintenanceId;
    this.make = make;
    this.vehicleName = vehicleName;
    this.model = model;
    this.type = type;
    this.startYear = startYear;
    this.endYear = endYear;
    this.oilViscosity = oilViscosity;
    this.oilQuantityWithFilter = oilQuantityWithFilter;
    this.oilQuantityWithoutFilter = oilQuantityWithoutFilter;
    this.oilFilterPartNumber = oilFilterPartNumber;
    this.carWashSize = carWashSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MaintenanceGuide that)) {
      return false;
    }
    return maintenanceId == that.maintenanceId
        && Double.compare(that.oilQuantityWithFilter, oilQuantityWithFilter) == 0
        && Double.compare(that.oilQuantityWithoutFilter, oilQuantityWithoutFilter) == 0
        && Objects.equals(make, that.make) && Objects.equals(vehicleName,
        that.vehicleName) && Objects.equals(model, that.model) && Objects.equals(
        type, that.type) && Objects.equals(startYear, that.startYear)
        && Objects.equals(endYear, that.endYear) && Objects.equals(oilViscosity,
        that.oilViscosity) && Objects.equals(oilFilterPartNumber, that.oilFilterPartNumber)
        && carWashSize == that.carWashSize;
  }

  @Override
  public int hashCode() {
    return Objects.hash(maintenanceId, make, vehicleName, model, type, startYear, endYear,
        oilViscosity, oilQuantityWithFilter, oilQuantityWithoutFilter, oilFilterPartNumber,
        carWashSize);
  }
}
