package com.u1pena.estimateapi.master.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ProductCategory {

  @Schema(description = "商品カテゴリーID", example = "1")
  private int categoryId;
  @Schema(description = "商品カテゴリー名", example = "エンジンオイル")
  private String categoryName;

  public ProductCategory(int categoryId, String categoryName) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
  }
}
