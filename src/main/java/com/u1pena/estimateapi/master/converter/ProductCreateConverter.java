package com.u1pena.estimateapi.master.converter;

import com.u1pena.estimateapi.master.dto.request.ProductCreateRequest;
import com.u1pena.estimateapi.master.entity.Product;

public class ProductCreateConverter {

  public static Product toEntity(
      ProductCreateRequest productCreateRequest) {
    return Product.builder()
        .categoryId(productCreateRequest.getCategoryId())
        .productName(productCreateRequest.getProductName())
        .description(productCreateRequest.getDescription())
        .guideMatchKey(productCreateRequest.getGuideMatchKey())
        .price(productCreateRequest.getPrice())
        .build();
  }
}
