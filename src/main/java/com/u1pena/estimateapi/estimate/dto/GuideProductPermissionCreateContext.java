package com.u1pena.estimateapi.estimate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuideProductPermissionCreateContext {

  private int maintenanceId;
  private int categoryId;
  private int productId;
  private double quantity;
  private boolean autoAdjustQuantity;
}
