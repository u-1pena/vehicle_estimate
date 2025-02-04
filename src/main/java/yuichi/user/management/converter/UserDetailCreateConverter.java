package yuichi.user.management.converter;

import java.time.LocalDate;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;

public class UserDetailCreateConverter {


  public static UserDetail userDetailConvertToEntity(User user,
      UserDetailCreateRequest userDetailCreateRequest) {
    return new UserDetail(user.getId(), userDetailCreateRequest.getFirstName(),
        userDetailCreateRequest.getLastName(), userDetailCreateRequest.getFirstNameKana(),
        userDetailCreateRequest.getLastNameKana(),
        LocalDate.parse(userDetailCreateRequest.getBirthday()),
        userDetailCreateRequest.getMobilePhoneNumber(), userDetailCreateRequest.getPassword());
  }
}
