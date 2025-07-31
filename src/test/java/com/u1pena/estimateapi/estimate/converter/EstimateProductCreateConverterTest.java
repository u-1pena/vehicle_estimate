package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.EstimateProductContext;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.master.entity.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EstimateProductCreateConverterTest {

  @Test
  void 見積もり商品詳細がDtoに変換されること() {
    // 準備
    EstimateProductContext estimateProductContext = EstimateProductContext.builder()
        .estimateBaseId(1)
        .product(Product.builder().productId(100).build())
        .quantity(2)
        .unitPrice(BigDecimal.valueOf(1500))
        .totalPrice(BigDecimal.valueOf(3000))
        .build();

    // 実行
    EstimateProduct actual = EstimateProductCreateConverter.toEntity(estimateProductContext);

    // 検証
    assertThat(actual).isNotNull();
    assertThat(actual.getEstimateBaseId()).isEqualTo(estimateProductContext.getEstimateBaseId());
    assertThat(actual.getProductId()).isEqualTo(estimateProductContext.getProduct().getProductId());
    assertThat(actual.getQuantity()).isEqualTo(estimateProductContext.getQuantity());
    assertThat(actual.getUnitPrice()).isEqualByComparingTo(estimateProductContext.getUnitPrice());
    assertThat(actual.getTotalPrice()).isEqualByComparingTo(estimateProductContext.getTotalPrice());
  }
}
