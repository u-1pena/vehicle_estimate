package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.customer.dto.request.VehicleCreateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.Vehicle;
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
