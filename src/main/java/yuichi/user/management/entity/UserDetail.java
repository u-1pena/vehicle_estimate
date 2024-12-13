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

}
