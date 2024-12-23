package yuichi.user.management.entity;

import java.time.YearMonth;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPayment {

  private int id;
  private int userId;
  private String cardNumber;
  private String cardBrand;
  private String cardHolder;
  private YearMonth expirationDate;

  public UserPayment(int id, int userId, String cardNumber, String cardBrand, String cardHolder,
      YearMonth expirationDate) {
    this.id = id;
    this.userId = userId;
    this.cardNumber = cardNumber;
    this.cardBrand = cardBrand;
    this.cardHolder = cardHolder;
    this.expirationDate = expirationDate;
  }
}
