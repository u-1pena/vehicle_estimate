package com.u1pena.estimateapi.estimate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

  @Schema(description = "名前　フルネーム", example = "山田 太郎")
  private String fullName;
  @Schema(description = "名前　フルネーム（カタカナ）", example = "ヤマダ タロウ")
  private String fullNameKana;
  @Schema(description = "Eメール", example = "tarou@example.com")
  private String email;
  @Schema(description = "電話番号", example = "090-1234-5678")
  private String phoneNumber;
}
