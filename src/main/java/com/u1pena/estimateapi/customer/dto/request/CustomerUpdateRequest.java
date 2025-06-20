package com.u1pena.estimateapi.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerUpdateRequest {

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "lastNameは半角英小文字で入力してください")
  private String lastName;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "firstNameは半角英小文字で入力してください")
  private String firstName;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "lastNameKanaは全角カタカナで入力してください")
  private String lastNameKana;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "firstNameKanaは全角カタカナで入力してください")
  private String firstNameKana;

  @NotBlank(message = "空白は許可されていません")
  @Email(message = "emailはメールアドレス形式で入力してください")
  private String email;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$", message = "phoneNumberは半角数字でハイフン含め11文字で入力してください")
  private String phoneNumber;
}
