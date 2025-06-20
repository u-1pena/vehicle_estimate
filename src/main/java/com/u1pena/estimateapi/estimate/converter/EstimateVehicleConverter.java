package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;

public class EstimateVehicleConverter {

  public static VehicleResponse toDto(String vehicleName, Vehicle vehicle) {
    return VehicleResponse.builder()
        .vehicleName(vehicleName)
        .make(vehicle.getMake())
        .model(vehicle.getModel())
        .type(vehicle.getType())
        .year(vehicle.getYear())
        .build();
  }

}
