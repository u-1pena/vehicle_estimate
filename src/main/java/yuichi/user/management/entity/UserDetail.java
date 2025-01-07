package yuichi.user.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Objects;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDetail that = (UserDetail) o;
    return id == that.id && Objects.equals(firstName, that.firstName)
        && Objects.equals(lastName, that.lastName) && Objects.equals(
        firstNameKana, that.firstNameKana) && Objects.equals(lastNameKana,
        that.lastNameKana) && Objects.equals(birthday, that.birthday)
        && Objects.equals(mobilePhoneNumber, that.mobilePhoneNumber)
        && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, firstNameKana, lastNameKana, birthday,
        mobilePhoneNumber, password);
  }
}
