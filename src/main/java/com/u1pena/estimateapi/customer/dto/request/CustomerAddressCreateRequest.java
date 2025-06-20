package com.u1pena.estimateapi.customer.dto.request;

import com.u1pena.estimateapi.common.validator.ValidPrefecture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerAddressCreateRequest {

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^\\d{3}-\\d{4}$", message = "postalCodeは半角数字でハイフン含め7文字で入力してください")
  private String postalCode;
  @NotBlank(message = "空白は許可されていません")
  @ValidPrefecture
  private String prefecture;
  @NotBlank(message = "空白は許可されていません")
  private String city;
  @NotBlank(message = "空白は許可されていません")
  private String townAndNumber;
  private String buildingNameAndRoomNumber;
}
