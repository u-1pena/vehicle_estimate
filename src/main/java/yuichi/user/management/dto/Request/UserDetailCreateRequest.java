package yuichi.user.management.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDetailCreateRequest {

  @NotBlank
  @Pattern(regexp = "^[a-z]+$", message = "firstNameは半角英小文字で入力してください")
  private String firstName;

  @NotBlank
  @Pattern(regexp = "^[a-z]+$", message = "lastNameは半角英小文字で入力してください")
  private String lastName;

  @NotBlank
  @Pattern(regexp = "^[ｦ-ﾝ]+$", message = "firstNameKanaは半角カタカナで入力してください")
  private String firstNameKana;

  @NotBlank
  @Pattern(regexp = "^[ｦ-ﾝ]+$", message = "lastNameKanaは半角カタカナで入力してください")
  private String lastNameKana;

  @NotNull(message = "birthdayは必須です")
  @Past(message = "birthdayは過去の日付で入力してください")
  private LocalDate birthday;

  @NotBlank
  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$", message = "mobilePhoneNumberは半角数字でハイフン含め11文字で入力してください")
  private String mobilePhoneNumber;

  @NotBlank
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
      message = "passwordは半角英大文字、小文字、数字、記号をそれぞれ1文字以上含む8文字以上16文字以下で入力してください")
  private String password;
}
