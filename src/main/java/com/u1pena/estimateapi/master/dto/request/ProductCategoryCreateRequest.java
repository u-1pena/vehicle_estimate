package com.u1pena.estimateapi.master.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCategoryCreateRequest {

  @Schema(description = "商品カテゴリー名", example = "オイルフィルター")
  @NotBlank(message = "カテゴリー名を入力してください")
  private String categoryName;

}
