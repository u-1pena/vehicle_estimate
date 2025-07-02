package com.u1pena.estimateapi.customer.dto.request;

import com.u1pena.estimateapi.common.validator.ValidPrefecture;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Tag(name = "CustomerAddressUpdateRequest", description = "顧客住所の更新リクエスト")
@Data
public class CustomerAddressUpdateRequest {

  @Schema(description = "郵便番号", example = "123-4567")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^\\d{3}-\\d{4}$", message = "postalCodeは半角数字でハイフン含め7文字で入力してください")
  private String postalCode;
  @Schema(description = "都道府県　それ以外を入力するとvalidationエラーになります。", example = "東京都")
  @NotBlank(message = "空白は許可されていません")
  @ValidPrefecture
  private String prefecture;
  @Schema(description = "市区町村", example = "新宿区")
  @NotBlank(message = "空白は許可されていません")
  private String city;
  @Schema(description = "町名・番地", example = "西新宿2-8-1")
  @NotBlank(message = "空白は許可されていません")
  private String townAndNumber;
  @Schema(description = "建物名・部屋番号　なければブランクでも可", example = "新宿ビル101号室")
  private String buildingNameAndRoomNumber;
}
