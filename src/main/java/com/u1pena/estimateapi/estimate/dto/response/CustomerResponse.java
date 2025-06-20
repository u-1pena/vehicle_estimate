package com.u1pena.estimateapi.estimate.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

  private String fullName;
  private String fullNameKana;
  private String email;
  private String phoneNumber;
}
