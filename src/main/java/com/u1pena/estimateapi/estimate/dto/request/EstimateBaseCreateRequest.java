package com.u1pena.estimateapi.estimate.dto.request;

import lombok.Data;

@Data
public class EstimateBaseCreateRequest {

  private int customerId;
  private int vehicleId;
  private int maintenanceId;

  public static EstimateBaseCreateRequest fromVehicleId(int vehicleId) {
    EstimateBaseCreateRequest request = new EstimateBaseCreateRequest();
    request.setVehicleId(vehicleId);
    return request;
  }
}

