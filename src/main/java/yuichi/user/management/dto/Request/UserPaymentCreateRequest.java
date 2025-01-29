package yuichi.user.management.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPaymentCreateRequest {

  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^\\d{16}$", message = "cardNumberは半角数字で16文字で入力してください")
  private String cardNumber;
  //CREATE処理時は入力不要
  @Pattern(regexp = "^(VISA|MasterCard|AmericanExpress|JCB|Diners)$", message = "cardBrandはVISA,MasterCard,AmericanExpress,JCB,Dinersのいずれかで入力してください")
  private String cardBrand;
  @NotBlank(message = "空白は許可されていません")
  //半角英大文字とスペースのみ許可
  @Pattern(regexp = "^[A-Z ]+$", message = "cardHolderは半角英大文字とスペースで入力してください")
  private String cardHolder;
  @NotBlank(message = "空白は許可されていません")
  @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "expirationDateは半角数字でYYYY-MMの形式で入力してください")
  private String expirationDate;
}
