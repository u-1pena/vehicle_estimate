package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimateHeaderResponse {

  @Schema(description = "見積もりID", example = "1")
  private int estimateBaseId;
  @Schema(description = "見積り日時", example = "2023-10-01")
  private LocalDate estimateDate;
  @Schema(description = "顧客情報", example = "田中 太郎 ﾀﾅｶﾀﾛｳ　090-1234-5678 tarou@example.com")
  private CustomerResponse customer;
  @Schema(description = "顧客住所情報", example = "123-4567 東京都新宿区西新宿2-8-1 新宿ビル101号室")
  private CustomerAddressResponse customerAddress;
  @Schema(description = "車両情報")
  private VehicleResponse vehicle;
}
