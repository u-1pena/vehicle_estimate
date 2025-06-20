package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.customer.dto.request.VehicleUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import java.time.LocalDate;
import java.time.YearMonth;

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
