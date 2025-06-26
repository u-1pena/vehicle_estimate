package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAddressResponse {

  @Schema(description = "郵便番号", example = "123-4567")
  private String postalCode;
  @Schema(description = "住所（都道府県、市区町村、町名・番地、建物名・部屋番号を含む）", example = "東京都新宿区西新宿2-8-1 新宿ビル101号室")
  private String fullAddress;
}
