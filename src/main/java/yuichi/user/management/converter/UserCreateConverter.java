package yuichi.user.management.converter;

import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.entity.User;

public class UserCreateConverter {

  public static User userConvertToEntity(UserCreateRequest userCreateRequest) {
    User user = new User();
    user.setAccount(userCreateRequest.getAccount());
    user.setEmail(userCreateRequest.getEmail());
    return user;
  }
}
