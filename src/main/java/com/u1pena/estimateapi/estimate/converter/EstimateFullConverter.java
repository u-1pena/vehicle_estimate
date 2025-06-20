package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import java.math.BigDecimal;
import java.util.List;

public class EstimateFullConverter {

  public static EstimateFullResponse toDto(EstimateHeaderResponse estimateHeader,
      List<EstimateProductResponse> estimateProducts, BigDecimal totalPrice) {
    return EstimateFullResponse.builder()
        .estimateHeader(estimateHeader)
        .estimateProducts(estimateProducts)
        .totalPrice(totalPrice)
        .build();
  }

}
