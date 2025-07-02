package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;

public class EstimateSummaryConverter {

  public static EstimateSummaryResponse toDto(EstimateSummaryResult estimateSummaryResult) {
    return EstimateSummaryResponse.builder()
        .estimateBaseId(estimateSummaryResult.getEstimateBaseId())
        .estimateDate(estimateSummaryResult.getEstimateDate())
        .vehicleName(estimateSummaryResult.getVehicleName())
        .estimateSummary(estimateSummaryResult.getEstimateSummary())
        .totalPrice(estimateSummaryResult.getTotalPrice())
        .build();
  }
}
