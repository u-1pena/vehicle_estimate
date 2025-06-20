package com.u1pena.estimateapi.master.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class GuideProductPermission {

  private int maintenanceId;
  private int categoryId;
  private int productId;
  private Double quantity;
  private boolean autoAdjustQuantity;

  public GuideProductPermission(int maintenanceId, int categoryId, int productId,
      Double quantity,
      boolean autoAdjustQuantity) {
    this.maintenanceId = maintenanceId;
    this.categoryId = categoryId;
    this.productId = productId;
    this.quantity = quantity;
    this.autoAdjustQuantity = autoAdjustQuantity;
  }
}
