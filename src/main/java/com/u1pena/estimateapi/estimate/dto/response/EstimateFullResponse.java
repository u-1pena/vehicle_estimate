package com.u1pena.estimateapi.estimate.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateFullResponse {

  private EstimateHeaderResponse estimateHeader;
  private List<EstimateProductResponse> estimateProducts;
  private BigDecimal totalPrice;

}
