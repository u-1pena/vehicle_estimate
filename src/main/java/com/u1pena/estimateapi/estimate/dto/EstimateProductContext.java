package com.u1pena.estimateapi.estimate.dto;

import com.u1pena.estimateapi.master.entity.Product;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateProductContext {

  private int estimateBaseId;
  private Product product;
  private double quantity;
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;
}
