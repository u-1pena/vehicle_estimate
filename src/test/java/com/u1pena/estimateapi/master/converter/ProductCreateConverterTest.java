package com.u1pena.estimateapi.master.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.master.dto.request.ProductCreateRequest;
import com.u1pena.estimateapi.master.entity.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductCreateConverterTest {

  @Test
  void productCreateRequestをproductに変換できる() {
    ProductCreateRequest productCreateRequest = new ProductCreateRequest();
    productCreateRequest.setCategoryId(1);
    productCreateRequest.setProductName("Test Product");
    productCreateRequest.setDescription("This is a test product.");
    productCreateRequest.setGuideMatchKey("test-key");
    productCreateRequest.setPrice(BigDecimal.valueOf(1000));

    Product actual = ProductCreateConverter.toEntity(productCreateRequest);
    // 検証
    assertThat(actual.getCategoryId()).isEqualTo(productCreateRequest.getCategoryId());
    assertThat(actual.getProductName()).isEqualTo(productCreateRequest.getProductName());
    assertThat(actual.getDescription()).isEqualTo(productCreateRequest.getDescription());
    assertThat(actual.getGuideMatchKey()).isEqualTo(productCreateRequest.getGuideMatchKey());
    assertThat(actual.getPrice()).isEqualTo(productCreateRequest.getPrice());
  }
}
