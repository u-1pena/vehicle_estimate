package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateProductResponse {

  @Schema(description = "商品カテゴリーID", example = "1")
  private String categoryName;
  @Schema(description = "商品名", example = "オイルフィルター")
  private String productName;
  @Schema(description = "数量", example = "2.0")
  private double quantity;
  @Schema(description = "単価", example = "1000.00")
  private BigDecimal unitPrice;
  @Schema(description = "合計金額", example = "2000.00")
  private BigDecimal totalPrice;
}
