package com.u1pena.estimateapi.master.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "商品ID", example = "1")
  private int productId;
  @Schema(description = "商品カテゴリーID", example = "1")
  private int categoryId;
  @Schema(description = "商品名", example = "オイルフィルター")
  private String productName;
  @Schema(description = "商品詳細", example = "エンジンオイルフィルター")
  private String description;
  @Schema(description = "ガイドキー", example = "OIL_FILTER")
  private String guideMatchKey;
  @Schema(description = "価格", example = "1000.00")
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
