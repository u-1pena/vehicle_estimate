package com.u1pena.estimateapi.master.converter;

import com.u1pena.estimateapi.master.dto.request.ProductCategoryCreateRequest;
import com.u1pena.estimateapi.master.entity.ProductCategory;

public class ProductCategoryCreateConverter {

  public static ProductCategory toEntity(
      ProductCategoryCreateRequest productCategoryCreateRequest) {
    return ProductCategory.builder()
        .categoryName(productCategoryCreateRequest.getCategoryName())
        .build();
  }
}
