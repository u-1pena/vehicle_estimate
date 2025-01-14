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
    UserPayment userPayment = new UserPayment();
    userPayment.setUserId(userDetail.getId());
    userPayment.setCardNumber(userPaymentCreateRequest.getCardNumber());
    userPayment.setCardBrand(
        userService.identifyCardBrand(userPaymentCreateRequest.getCardNumber()));
    userPayment.setCardHolder(userPaymentCreateRequest.getCardHolder());
    userPayment.setExpirationDate(YearMonth.parse(userPaymentCreateRequest.getExpirationDate()));
    return userPayment;
  }

}
