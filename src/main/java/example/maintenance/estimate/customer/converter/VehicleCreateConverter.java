package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.request.VehicleCreateRequest;
import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.Vehicle;
import example.maintenance.estimate.customer.entity.enums.PlateRegion;
import java.time.LocalDate;
import java.time.YearMonth;

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
        .active(true)
        .build();
  }
}
