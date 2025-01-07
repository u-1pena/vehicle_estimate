package yuichi.user.management.dto;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Getter
@Setter
public class UserInformationDto {

  private User user;
  private UserDetail userDetail;
  private List<UserPayment> userPayment;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInformationDto that = (UserInformationDto) o;
    return Objects.equals(user, that.user) && Objects.equals(userDetail,
        that.userDetail) && Objects.equals(userPayment, that.userPayment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, userDetail, userPayment);
  }
}
