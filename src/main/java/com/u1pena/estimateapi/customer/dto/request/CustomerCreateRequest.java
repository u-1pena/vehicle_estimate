package com.u1pena.estimateapi.customer.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Tag(name = "CustomerCreateRequest", description = "顧客の作成リクエスト")
@Data
public class CustomerCreateRequest {

  @Schema(description = "顧客の姓", example = "tanaka")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "lastNameは半角英小文字で入力してください")
  private String lastName;

  @Parameter(description = "顧客の名", example = "tarou")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "firstNameは半角英小文字で入力してください")
  private String firstName;

  @Parameter(description = "顧客の姓（カタカナ）", example = "タナカ")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "lastNameKanaは全角カタカナで入力してください")
  private String lastNameKana;

  @Parameter(description = "顧客の名（カタカナ）", example = "タロウ")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "firstNameKanaは全角カタカナで入力してください")
  private String firstNameKana;

  @Parameter(description = "Eメールアドレス", example = "tarou@example.com")
  @NotBlank(message = "空白は許可されていません")
  @Email(message = "emailはメールアドレス形式で入力してください")
  private String email;

  @Parameter(description = "電話番号", example = "090-1234-5678")
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$", message = "phoneNumberは半角数字でハイフン含め11文字で入力してください")
  private String phoneNumber;
}
