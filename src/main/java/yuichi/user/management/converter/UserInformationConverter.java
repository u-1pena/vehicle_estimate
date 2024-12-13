package yuichi.user.management.converter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Getter
@Setter
public class UserInformationConverter {

  public static UserInformationDto convertToUserInformationDto(User user, UserDetail userDetail,
      List<UserPayment> userPayment) {
    UserInformationDto userInformationDto = new UserInformationDto();
    userInformationDto.setUser(user);
    userInformationDto.setUserDetail(userDetail);
    userInformationDto.setUserPayment(userPayment);
    return userInformationDto;
  }
}
