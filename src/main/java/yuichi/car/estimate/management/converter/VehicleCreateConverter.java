package yuichi.car.estimate.management.converter;

import java.time.LocalDate;
import java.time.YearMonth;
import yuichi.car.estimate.management.dto.request.VehicleCreateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.entity.enums.PlateRegion;

public class VehicleCreateConverter {

  public static Vehicle vehicleConvertToEntity(Customer customer,
      VehicleCreateRequest vehicleCreateRequest) {
    return Vehicle.builder()
        .customerId(customer.getCustomerId())
        .plateRegion(PlateRegion.valueOf(vehicleCreateRequest.getPlateRegion()))
        .plateCategoryNumber(vehicleCreateRequest.getPlateCategoryNumber())
        .plateHiragana(vehicleCreateRequest.getPlateHiragana())
        .plateVehicleNumber(vehicleCreateRequest.getPlateVehicleNumber())
        .make(vehicleCreateRequest.getMake())
        .model(vehicleCreateRequest.getModel())
        .type(vehicleCreateRequest.getType())
        .year(YearMonth.parse(vehicleCreateRequest.getYear()))
        .inspectionDate(LocalDate.parse(vehicleCreateRequest.getInspectionDate()))
        .build();
  }
}
