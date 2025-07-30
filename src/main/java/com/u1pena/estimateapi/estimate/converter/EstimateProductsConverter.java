package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;

public class EstimateProductsConverter {

  public static EstimateProductResponse toDto(EstimateProductJoinResult estimateProductJoinResult,
      EstimateProduct estimateProduct) {
    return EstimateProductResponse.builder()
        .categoryName(estimateProductJoinResult.getCategoryName())
        .productName(estimateProductJoinResult.getProductName())
        .quantity(estimateProduct.getQuantity())
        .unitPrice(estimateProduct.getUnitPrice())
        .totalPrice(estimateProduct.getTotalPrice())
        .build();
  }
}
