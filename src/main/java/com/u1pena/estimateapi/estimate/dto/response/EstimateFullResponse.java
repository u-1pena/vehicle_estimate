package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateFullResponse {

  @Schema(description = "見積もりヘッダー情報")
  private EstimateHeaderResponse estimateHeader;
  @Schema(description = "見積もり商品情報")
  private List<EstimateProductResponse> estimateProducts;
  @Schema(description = "見積もり合計金額", example = "50000.00")
  private BigDecimal totalPrice;

}
