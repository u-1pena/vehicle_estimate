package com.u1pena.estimateapi.estimate.dto.request;

import com.u1pena.estimateapi.common.validator.YenAmount;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class EstimateProductCreateRequest {

  @NotNull
  private int estimateBaseId;
  @NotNull
  private int productId;
  private Double quantity;
  @YenAmount
  private BigDecimal unitPrice;
  @YenAmount
  private BigDecimal totalPrice;
}
