package yuichi.user.management.converter;

import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.entity.User;

public class UserCreateConverter {

  public static User userConvertToEntity(UserCreateRequest userCreateRequest) {
    return new User(userCreateRequest.getUserAccount(), userCreateRequest.getEmail());
  }
}
