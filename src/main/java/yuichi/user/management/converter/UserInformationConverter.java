package yuichi.user.management.converter;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@Getter
@Setter
public class UserInformationConverter {

  private User user;
  private UserDetail userDetail;
  private List<UserPayment> userPayment;

}

