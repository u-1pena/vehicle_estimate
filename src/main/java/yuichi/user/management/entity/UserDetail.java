package yuichi.user.management.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetail {

  private int id;
  private String firstName;
  private String lastName;
  private String firstNameKana;
  private String lastNameKana;
  private LocalDate birthday;
  private String mobilePhoneNumber;
  private String password;

  public UserDetail(int id, String firstName, String lastName, String firstNameKana,
      String lastNameKana, LocalDate birthday, String mobilePhoneNumber, String password) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.firstNameKana = firstNameKana;
    this.lastNameKana = lastNameKana;
    this.birthday = birthday;
    this.mobilePhoneNumber = mobilePhoneNumber;
    this.password = password;
  }
}
