package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EstimateSummaryDateConverterTest {

  @Test
  void EstimateSummaryResultをEstimateSummaryDateResponseに変換できること() {
    // 準備
    EstimateSummaryResult estimateSummaryResult = EstimateSummaryResult.builder()
        .estimateDate(LocalDate.of(2023, 10, 1))
        .estimateBaseId(1)
        .customerName("yamada tarou")
        .vehicleName("toyota プリウス")
        .estimateSummary("oil、filter")
        .totalPrice(new BigDecimal("5000.00"))
        .build();

    // 実行
    EstimateSummaryDateResponse actual = EstimateSummaryDateConverter.toDto(estimateSummaryResult);

    // 検証
    assertThat(actual.getEstimateDate()).isEqualTo(LocalDate.of(2023, 10, 1));
    assertThat(actual.getEstimateBaseId()).isEqualTo(1);
    assertThat(actual.getCustomerName()).isEqualTo("yamada tarou");
    assertThat(actual.getVehicleName()).isEqualTo("toyota プリウス");
    assertThat(actual.getEstimateSummary()).isEqualTo("oil、filter");
    assertThat(actual.getTotalPrice()).isEqualByComparingTo(new BigDecimal("5000.00"));
  }
}
