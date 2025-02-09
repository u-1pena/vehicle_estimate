package yuichi.user.management.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDetailCreateRequest {

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "firstNameは半角英小文字で入力してください")
  private String firstName;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^[a-z]+$", message = "lastNameは半角英小文字で入力してください")
  private String lastName;

  @NotBlank(message = "空白は許可されていません")
  //濁点、半濁点を含む全角カタカナ
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "firstNameKanaは全角カタカナで入力してください")
  private String firstNameKana;

  @NotBlank(message = "空白は許可されていません")
  //濁点、半濁点を含む全角カタカナ
  @Pattern(regexp = "^[ァ-ヶー]+$", message = "lastNameKanaは全角カタカナで入力してください")
  private String lastNameKana;

  @NotBlank(message = "birthdayは必須です")
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "birthdayはyyyy-MM-ddで入力してください")
  private String birthday;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$", message = "mobilePhoneNumberは半角数字でハイフン含め11文字で入力してください")
  private String mobilePhoneNumber;

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
      message = "passwordは半角英大文字、小文字、数字、記号をそれぞれ1文字以上含む8文字以上16文字以下で入力してください")
  private String password;
}
