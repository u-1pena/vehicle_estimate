package com.u1pena.estimateapi.estimate.dto.response;

import java.time.YearMonth;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResponse {

  private String vehicleName;
  private String make;
  private String model;
  private String type;
  private YearMonth year;
}
