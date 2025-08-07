package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EstimateProductsConverterTest {

  @Test
  void EstimateProductResponseの変換テスト() {
    // 準備
    EstimateProductJoinResult estimateProductJoinResult = EstimateProductJoinResult.builder()
        .categoryName("Test Category")
        .productName("Test Product")
        .build();
    EstimateProduct estimateProduct = EstimateProduct.builder()
        .quantity(2)
        .unitPrice(BigDecimal.valueOf(1000))
        .totalPrice(BigDecimal.valueOf(2000))
        .build();

    EstimateProductResponse estimateProductResponse = EstimateProductResponse.builder()
        .categoryName(estimateProductJoinResult.getCategoryName())
        .productName(estimateProductJoinResult.getProductName())
        .quantity(estimateProduct.getQuantity())
        .unitPrice(estimateProduct.getUnitPrice())
        .totalPrice(estimateProduct.getTotalPrice())
        .build();

    // 実行
    EstimateProductResponse actual = EstimateProductsConverter.toDto(estimateProductJoinResult,
        estimateProduct);

    // 検証
    assertThat(actual.getCategoryName()).isEqualTo(estimateProductResponse.getCategoryName());
    assertThat(actual.getProductName()).isEqualTo(estimateProductResponse.getProductName());
    assertThat(actual.getQuantity()).isEqualTo(estimateProductResponse.getQuantity());
    assertThat(actual.getUnitPrice()).isEqualByComparingTo(estimateProductResponse.getUnitPrice());
    assertThat(actual.getTotalPrice()).isEqualByComparingTo(
        estimateProductResponse.getTotalPrice());
    assertThat(actual.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(2000));
  }
}
