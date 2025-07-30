package com.u1pena.estimateapi.estimate.dto.request;

import com.u1pena.estimateapi.common.validator.YenAmount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateProductCreateRequest {

  @Schema(description = "見積もりベースID", example = "1")
  @NotNull
  private int estimateBaseId;
  @Schema(description = "商品ID", example = "1")
  @NotNull
  private int productId;
  @Schema(description = "数量", example = "2.0")
  private Double quantity;
  @Schema(description = "単価", example = "1000.00")
  @YenAmount
  private BigDecimal unitPrice;
  @Schema(description = "合計金額", example = "2000.00")
  @YenAmount
  private BigDecimal totalPrice;
}
