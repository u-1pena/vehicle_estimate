package com.u1pena.estimateapi.estimate.entity;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class EstimateBase {

  private int estimateBaseId;
  private int customerId;
  private int vehicleId;
  private int maintenanceId;
  private LocalDate estimateDate;

  public EstimateBase(int estimateBaseId, int customerId, int vehicleId, int maintenanceId,
      LocalDate estimateDate) {
    this.estimateBaseId = estimateBaseId;
    this.customerId = customerId;
    this.vehicleId = vehicleId;
    this.maintenanceId = maintenanceId;
    this.estimateDate = estimateDate;
  }
}
