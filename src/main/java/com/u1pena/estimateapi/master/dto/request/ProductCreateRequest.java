package com.u1pena.estimateapi.master.dto.request;

import com.u1pena.estimateapi.common.validator.YenAmount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductCreateRequest {

  @Schema(description = "商品カテゴリーID", example = "1")
  @NotNull(message = "nullは許可されていません")
  private int categoryId;
  @Schema(description = "商品名", example = "オイルフィルター")
  @NotBlank(message = "商品名は必須です")
  private String productName;
  @Schema(description = "商品詳細", example = "エンジンオイルフィルター")
  @NotBlank(message = "商品詳細は必須です")
  private String description;
  @Schema(description = "ガイドキー", example = "OIL_FILTER")
  @NotBlank(message = "ガイドキーは必須です")
  private String guideMatchKey;
  @Schema(description = "価格", example = "1000.00")
  @NotNull(message = "nullは許可されていません")
  @YenAmount
  private BigDecimal price;
}
