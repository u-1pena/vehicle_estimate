package com.u1pena.estimateapi.master.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Product {

  private int productId;
  private int categoryId;
  private String productName;
  private String description;
  private String guideMatchKey;
  private BigDecimal price;

  public Product(int productId, int categoryId, String productName, String description,
      String guideMatchKey, BigDecimal price) {
    this.productId = productId;
    this.categoryId = categoryId;
    this.productName = productName;
    this.description = description;
    this.guideMatchKey = guideMatchKey;
    this.price = price;
  }
}
