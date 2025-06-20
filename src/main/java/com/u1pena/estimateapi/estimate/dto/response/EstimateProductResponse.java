package com.u1pena.estimateapi.estimate.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateProductResponse {

  private String categoryName;
  private String productName;
  private double quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
}
