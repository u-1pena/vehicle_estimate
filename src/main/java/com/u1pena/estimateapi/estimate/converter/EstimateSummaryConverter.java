package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;

public class EstimateSummaryConverter {

  public static EstimateSummaryResponse toDto(EstimateFullResponse estimateFullResponse,
      String estimateSummary) {
    return EstimateSummaryResponse.builder()
        .estimateBaseId(estimateFullResponse.getEstimateHeader().getEstimateBaseId())
        .estimateDate(estimateFullResponse.getEstimateHeader().getEstimateDate())
        .vehicleName(estimateFullResponse.getEstimateHeader().getVehicle().getVehicleName())
        .estimateSummary(estimateSummary)
        .totalPrice(estimateFullResponse.getTotalPrice())
        .build();
  }
}
