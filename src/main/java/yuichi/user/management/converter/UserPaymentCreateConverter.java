package yuichi.user.management.converter;

import java.time.YearMonth;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.service.UserService;

public class UserPaymentCreateConverter {

  UserPaymentCreateRequest userPaymentCreateRequest;

  public static UserPayment userPaymentConvertToEntity(UserDetail userDetail,
      UserPaymentCreateRequest userPaymentCreateRequest, UserService userService) {
    return new UserPayment(userDetail.getId(), userPaymentCreateRequest.getCardNumber(),
        userService.identifyCardBrand(userPaymentCreateRequest.getCardNumber()),
        userPaymentCreateRequest.getCardHolder(),
        YearMonth.parse(userPaymentCreateRequest.getExpirationDate()));
  }
}
