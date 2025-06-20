package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.EstimateProductContext;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;


public class EstimateProductCreateConverter {

  public static EstimateProduct toEntity(
      EstimateProductContext estimateProductContext) {
    return EstimateProduct.builder()
        .estimateBaseId(estimateProductContext.getEstimateBaseId())
        .productId(estimateProductContext.getProduct().getProductId())
        .quantity(estimateProductContext.getQuantity())
        .unitPrice(estimateProductContext.getUnitPrice())
        .totalPrice(estimateProductContext.getTotalPrice())
        .build();
  }
}
