package com.u1pena.estimateapi.estimate.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAddressResponse {

  private String postalCode;
  private String fullAddress;
}
