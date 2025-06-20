package com.u1pena.estimateapi.estimate.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateHeaderResponse {

  private int estimateBaseId;
  private LocalDate estimateDate;
  private CustomerResponse customer;
  private CustomerAddressResponse customerAddress;
  private VehicleResponse vehicle;
}
