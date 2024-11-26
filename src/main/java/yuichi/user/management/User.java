package yuichi.user.management;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private int id;
  private String name;
  private String email;

  public User(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

}
