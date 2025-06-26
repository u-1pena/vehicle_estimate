package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;

public class EstimateSummaryDateConverter {

  public static EstimateSummaryDateResponse toDto(
      EstimateFullResponse estimateFullResponse, String estimateSummary) {
    return EstimateSummaryDateResponse.builder()
        .estimateDate(estimateFullResponse.getEstimateHeader().getEstimateDate())
        .estimateBaseId(estimateFullResponse.getEstimateHeader().getEstimateBaseId())
        .customerName(estimateFullResponse.getEstimateHeader().getCustomer().getFullName())
        .vehicleName(estimateFullResponse.getEstimateHeader().getVehicle().getVehicleName())
        .estimateSummary(estimateSummary)
        .totalPrice(estimateFullResponse.getTotalPrice())
        .build();
  }

}
