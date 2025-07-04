package com.u1pena.estimateapi.estimate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EstimateSummaryResult {

  private int estimateBaseId;
  private LocalDate estimateDate;
  private String vehicleName;
  private String customerName;
  private String estimateSummary;
  private BigDecimal totalPrice;
}
