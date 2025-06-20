package com.u1pena.estimateapi.estimate.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class EstimateProductJoinResult {

  private String categoryName;
  private int productId;
  private int categoryId;
  private String productName;
  private String description;
  private String guideMatchKey;
  private BigDecimal price;
}
