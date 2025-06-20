package com.u1pena.estimateapi.master.dto.request;

import com.u1pena.estimateapi.common.validator.YenAmount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductCreateRequest {

  @NotNull(message = "nullは許可されていません")
  private int categoryId;

  @NotBlank(message = "商品名は必須です")
  private String productName;

  @NotBlank(message = "商品詳細は必須です")
  private String description;

  @NotBlank(message = "ガイドキーは必須です")
  private String guideMatchKey;

  @NotNull(message = "nullは許可されていません")
  @YenAmount
  private BigDecimal price;
}
