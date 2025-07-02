package com.u1pena.estimateapi.estimate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Valid
public class EstimateProductUpdateRequest {

  @Schema(description = "数量", example = "2.0")
  private double quantity;
  @Schema(description = "単価", example = "1000.00")
  private BigDecimal unitPrice;
  @Schema(description = "合計金額", example = "2000.00")
  private BigDecimal totalPrice;
}
