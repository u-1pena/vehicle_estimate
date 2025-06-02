package example.maintenance.estimate.customer.entity.master;

import example.maintenance.estimate.customer.entity.enums.CarWashSize;
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

  private int maintenanceId;
  private String make;
  private String vehicleName;
  private String model;
  private String type;
  private YearMonth startYear;
  private YearMonth endYear;
  private String oilViscosity;
  private double oilQuantityWithFilter;
  private double oilQuantityWithoutFilter;
  private String oilFilterPartNumber;
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
