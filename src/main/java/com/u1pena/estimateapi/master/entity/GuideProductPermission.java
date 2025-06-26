package com.u1pena.estimateapi.master.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class GuideProductPermission {

  @Schema(description = "メンテナンスID", example = "1")
  private int maintenanceId;
  @Schema(description = "商品カテゴリーID", example = "1")
  private int categoryId;
  @Schema(description = "商品ID", example = "1")
  private int productId;
  @Schema(description = "数量", example = "2.0")
  private Double quantity;
  @Schema(description = "数量自動調整フラグ", example = "true")
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
