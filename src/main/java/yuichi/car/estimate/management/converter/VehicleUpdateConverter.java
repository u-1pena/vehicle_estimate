package yuichi.car.estimate.management.converter;

import java.time.LocalDate;
import java.time.YearMonth;
import yuichi.car.estimate.management.dto.request.VehicleUpdateRequest;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.entity.enums.PlateRegion;

public class VehicleUpdateConverter {

  public static void vehicleUpdateConvertToEntity(Vehicle vehicle,
      VehicleUpdateRequest vehicleUpdateRequest) {
    vehicle.setPlateRegion(PlateRegion.valueOf(vehicleUpdateRequest.getPlateRegion()));
    vehicle.setPlateCategoryNumber(vehicleUpdateRequest.getPlateCategoryNumber());
    vehicle.setPlateHiragana(vehicleUpdateRequest.getPlateHiragana());
    vehicle.setPlateVehicleNumber(vehicleUpdateRequest.getPlateVehicleNumber());
    vehicle.setMake(vehicleUpdateRequest.getMake());
    vehicle.setModel(vehicleUpdateRequest.getModel());
    vehicle.setType(vehicleUpdateRequest.getType());
    vehicle.setYear(YearMonth.parse(vehicleUpdateRequest.getYear()));
    vehicle.setInspectionDate(LocalDate.parse(vehicleUpdateRequest.getInspectionDate()));
    vehicle.setActive(vehicleUpdateRequest.isActive());
  }
}
