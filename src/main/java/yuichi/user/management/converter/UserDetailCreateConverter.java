package yuichi.user.management.converter;

import java.time.LocalDate;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;

public class UserDetailCreateConverter {

  public static UserDetail userDetailConvertToEntity(User user,
      UserDetailCreateRequest userDetailCreateRequest) {
    UserDetail userDetail = new UserDetail();
    userDetail.setId(user.getId());
    userDetail.setFirstName(userDetailCreateRequest.getFirstName());
    userDetail.setLastName(userDetailCreateRequest.getLastName());
    userDetail.setFirstNameKana(userDetailCreateRequest.getFirstNameKana());
    userDetail.setLastNameKana(userDetailCreateRequest.getLastNameKana());
    userDetail.setBirthday(LocalDate.parse(userDetailCreateRequest.getBirthday()));
    userDetail.setMobilePhoneNumber(userDetailCreateRequest.getMobilePhoneNumber());
    userDetail.setPassword(userDetailCreateRequest.getPassword());
    return userDetail;
  }
}
