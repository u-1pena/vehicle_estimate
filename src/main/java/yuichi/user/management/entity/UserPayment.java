package yuichi.user.management.entity;

import java.time.YearMonth;
import java.util.Objects;
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

  //引数なしのコンストラクタ
  public UserPayment() {
  }

  public UserPayment(int userId, String cardNumber, String cardBrand, String cardHolder,
      YearMonth expirationDate) {
    this.userId = userId;
    this.cardNumber = cardNumber;
    this.cardBrand = cardBrand;
    this.cardHolder = cardHolder;
    this.expirationDate = expirationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPayment that = (UserPayment) o;
    return id == that.id && userId == that.userId && Objects.equals(cardNumber,
        that.cardNumber) && Objects.equals(cardBrand, that.cardBrand)
        && Objects.equals(cardHolder, that.cardHolder) && Objects.equals(
        expirationDate, that.expirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, cardNumber, cardBrand, cardHolder, expirationDate);
  }
}
