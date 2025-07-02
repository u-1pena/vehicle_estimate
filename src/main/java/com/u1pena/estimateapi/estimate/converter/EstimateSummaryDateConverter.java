package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;

public class EstimateSummaryDateConverter {

  public static EstimateSummaryDateResponse toDto(
      EstimateSummaryResult estimateSummaryResult) {
    return EstimateSummaryDateResponse.builder()
        .estimateDate(estimateSummaryResult.getEstimateDate())
        .estimateBaseId(estimateSummaryResult.getEstimateBaseId())
        .customerName(estimateSummaryResult.getCustomerName())
        .vehicleName(estimateSummaryResult.getVehicleName())
        .estimateSummary(estimateSummaryResult.getEstimateSummary())
        .totalPrice(estimateSummaryResult.getTotalPrice())
        .build();
  }
}
