package com.u1pena.estimateapi.estimate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EstimateBaseCreateRequest {

  @Schema(description = "顧客ID", example = "1")
  private int customerId;
  @Schema(description = "車両ID", example = "1")
  private int vehicleId;
  @Schema(description = "メンテナンスガイドID", example = "1")
  private int maintenanceId;

  public static EstimateBaseCreateRequest fromVehicleId(int vehicleId) {
    EstimateBaseCreateRequest request = new EstimateBaseCreateRequest();
    request.setVehicleId(vehicleId);
    return request;
  }
}

