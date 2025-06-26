package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateSummaryResponse {

  @Schema(description = "見積もりID", example = "1")
  private int estimateBaseId;
  @Schema(description = "見積もり作成日", example = "2023-10-01")
  private LocalDate estimateDate;
  @Schema(description = "車両名", example = "プリウス")
  private String vehicleName;
  @Schema(description = "見積もり概要", example = "MOTOR_OIL・OIL_FILTER")
  private String estimateSummary;
  @Schema(description = "見積もり合計金額", example = "5000.00")
  private BigDecimal totalPrice;
}
