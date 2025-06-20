package com.u1pena.estimateapi.master.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCategoryCreateRequest {

  @NotBlank(message = "カテゴリー名を入力してください")
  private String categoryName;

}
