package com.u1pena.estimateapi.estimate.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class EstimateProduct {

  private int estimateProductId;
  private int estimateBaseId;
  private int productId;
  private double quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;

  public EstimateProduct(int estimateProductId, int estimateBaseId, int productId, double quantity,
      BigDecimal unitPrice, BigDecimal totalPrice) {
    this.estimateProductId = estimateProductId;
    this.estimateBaseId = estimateBaseId;
    this.productId = productId;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.totalPrice = totalPrice;
  }
}
