package yuichi.user.management.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;
  private String account;
  private String email;

  public User(int id, String account, String email) {
    this.id = id;
    this.account = account;
    this.email = email;
  }
}
