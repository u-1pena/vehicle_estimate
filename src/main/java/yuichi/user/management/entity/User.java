package yuichi.user.management.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;
  private String account;
  private String email;

  public User() {
  }

  public User(int id, String account, String email) {
    this.id = id;
    this.account = account;
    this.email = email;
  }

  public User(String account, String email) {
    this.account = account;
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
    return id == user.id && Objects.equals(account, user.account)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, account, email);
  }
}
