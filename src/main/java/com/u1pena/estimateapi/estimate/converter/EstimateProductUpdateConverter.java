package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;

public class EstimateProductUpdateConverter {

  public static EstimateProduct toDto(EstimateProductUpdateRequest estimateProductUpdateRequest) {
    return EstimateProduct.builder()
        .quantity(estimateProductUpdateRequest.getQuantity())
        .unitPrice(estimateProductUpdateRequest.getUnitPrice())
        .totalPrice(estimateProductUpdateRequest.getTotalPrice())
        .build();
  }

}
