package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResponse {

  @Schema(description = "車両名", example = "プリウス")
  private String vehicleName;
  @Schema(description = "車両メーカー", example = "toyota")
  private String make;
  @Schema(description = "車両型式", example = "ZVW30")
  private String model;
  @Schema(description = "エンジン型式", example = "2ZR-FXE")
  private String type;
  @Schema(description = "車両年式", example = "2010-01")
  private YearMonth year;
}
