package yuichi.user.management.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.helper.TestHelper;
import yuichi.user.management.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserPaymentCreateConverterTest {

  @Mock
  UserService userService;

  @Test
  void UserPaymentCreateRequestをUserPaymentに変換できること() {
    // 準備
    UserDetail userDetail = new TestHelper().userDetailsMock().get(0);
    UserPaymentCreateRequest userPaymentCreateRequest = new UserPaymentCreateRequest();
    userPaymentCreateRequest.setCardNumber("4234567890123456");
    userPaymentCreateRequest.setCardHolder("test");
    userPaymentCreateRequest.setExpirationDate("2028-01");
    //モックの設定
    when(userService.identifyCardBrand("4234567890123456")).thenReturn("VISA");

    // 実行
    UserPayment actual = UserPaymentCreateConverter.userPaymentConvertToEntity(userDetail,
        userPaymentCreateRequest, userService);
    // 検証
    assertThat(actual.getCardNumber()).isEqualTo("4234567890123456");
    assertThat(actual.getCardHolder()).isEqualTo("test");
    assertThat(actual.getExpirationDate().toString()).isEqualTo("2028-01");
    assertThat(actual.getCardBrand()).isEqualTo("VISA");
  }
}
