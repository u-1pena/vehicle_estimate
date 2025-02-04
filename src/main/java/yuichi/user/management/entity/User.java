package yuichi.user.management.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;
  private String userAccount;
  private String email;

  public User() {
  }

  public User(int id, String userAccount, String email) {
    this.id = id;
    this.userAccount = userAccount;
    this.email = email;
  }

  public User(String userAccount, String email) {
    this.userAccount = userAccount;
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id && Objects.equals(this.userAccount, user.userAccount)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userAccount, email);
  }
}
