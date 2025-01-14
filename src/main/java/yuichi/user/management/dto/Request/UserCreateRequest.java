package yuichi.user.management.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateRequest {

  @NotBlank
  @Pattern(regexp = "^[a-zA-Z0-9]{4,32}$", message = "アカウントは半角英数字4文字以上32文字以下で入力してください")
  private String account;
  @NotBlank
  @Email(message = "メールアドレスの形式が不正です")
  private String email;
}
