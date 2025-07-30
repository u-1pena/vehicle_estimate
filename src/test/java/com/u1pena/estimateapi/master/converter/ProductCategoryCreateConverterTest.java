package com.u1pena.estimateapi.master.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.master.dto.request.ProductCategoryCreateRequest;
import com.u1pena.estimateapi.master.entity.ProductCategory;
import org.junit.jupiter.api.Test;

class ProductCategoryCreateConverterTest {

  @Test
  void productCategoryCreateRequestをproductCategoryに変換できること() {
    // Arrange
    ProductCategoryCreateRequest productCategoryCreateRequest = new ProductCategoryCreateRequest();
    productCategoryCreateRequest.setCategoryName("Test Category");

    // Act
    ProductCategory actual = ProductCategoryCreateConverter.toEntity(
        productCategoryCreateRequest);
    // Assert
    assertThat(actual.getCategoryName()).isEqualTo(productCategoryCreateRequest.getCategoryName());
  }
}
