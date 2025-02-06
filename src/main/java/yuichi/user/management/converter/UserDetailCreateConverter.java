package yuichi.user.management.converter;

import java.time.LocalDate;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;


public class UserDetailCreateConverter {


  public static UserDetail userDetailConvertToEntity(User user,
      UserDetailCreateRequest userDetailCreateRequest) {
    return UserDetail.builder()
        .id(user.getId())
        .firstName(userDetailCreateRequest.getFirstName())
        .lastName(userDetailCreateRequest.getLastName())
        .firstNameKana(userDetailCreateRequest.getFirstNameKana())
        .lastNameKana(userDetailCreateRequest.getLastNameKana())
        .birthday(LocalDate.parse(userDetailCreateRequest.getBirthday()))
        .mobilePhoneNumber(userDetailCreateRequest.getMobilePhoneNumber())
        .password(userDetailCreateRequest.getPassword())
        .build();
  }
}
