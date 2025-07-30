package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EstimateProductUpdateConverterTest {

  @Test
  void EstimateProductUpdateRequestをEstimateProductに変換するテスト() {
    // 準備
    EstimateProductUpdateRequest updateRequest = EstimateProductUpdateRequest.builder()
        .quantity(1.0)
        .unitPrice(BigDecimal.valueOf(1000.0))
        .totalPrice(BigDecimal.valueOf(1000.0))
        .build();

    // 実行
    EstimateProduct actual = EstimateProductUpdateConverter.toDto(updateRequest);

    // 検証
    assertThat(actual.getQuantity()).isEqualTo(updateRequest.getQuantity());
    assertThat(actual.getUnitPrice()).isEqualTo(updateRequest.getUnitPrice());
    assertThat(actual.getTotalPrice()).isEqualTo(updateRequest.getTotalPrice());
  }
}
