package com.u1pena.estimateapi.estimate.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateProductJoinResult {

  private String categoryName;
  private int productId;
  private int categoryId;
  private String productName;
  private String description;
  private String guideMatchKey;
  private BigDecimal price;
}
