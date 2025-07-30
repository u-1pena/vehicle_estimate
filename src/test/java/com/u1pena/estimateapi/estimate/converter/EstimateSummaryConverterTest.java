package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EstimateSummaryConverterTest {

  @Test
  void EstimateSummaryResultをEstimateSummaryResponseに変換するテスト() {
    // 準備
    EstimateSummaryResult estimateSummaryResult = EstimateSummaryResult.builder()
        .estimateBaseId(1)
        .estimateDate(LocalDate.of(2023, 10, 1))
        .vehicleName("Test Vehicle")
        .estimateSummary("Test Summary")
        .totalPrice(BigDecimal.valueOf(5000.00))
        .build();

    // 実行
    EstimateSummaryResponse actual = EstimateSummaryConverter.toDto(estimateSummaryResult);

    // 検証
    assertThat(actual.getEstimateBaseId()).isEqualTo(estimateSummaryResult.getEstimateBaseId());
    assertThat(actual.getEstimateDate()).isEqualTo(estimateSummaryResult.getEstimateDate());
    assertThat(actual.getVehicleName()).isEqualTo(estimateSummaryResult.getVehicleName());
    assertThat(actual.getEstimateSummary()).isEqualTo(estimateSummaryResult.getEstimateSummary());
    assertThat(actual.getTotalPrice()).isEqualTo(estimateSummaryResult.getTotalPrice());
  }

}
