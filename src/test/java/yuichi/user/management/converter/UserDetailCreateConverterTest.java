package yuichi.user.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;

class UserDetailCreateConverterTest {

  @Test
  void UserDetailCreateRequestからUserDetailに変換できること() {
    // 準備
    User user = new User();
    UserDetailCreateRequest userDetailCreateRequest = new UserDetailCreateRequest();
    userDetailCreateRequest.setFirstName("test");
    userDetailCreateRequest.setLastName("sample");
    userDetailCreateRequest.setFirstNameKana("ﾃｽﾄ");
    userDetailCreateRequest.setLastNameKana("ｻﾝﾌﾟﾙ");
    userDetailCreateRequest.setBirthday("2021-01-01");
    userDetailCreateRequest.setMobilePhoneNumber("090-1234-5678");
    userDetailCreateRequest.setPassword("Test@1234");
    // 実行
    UserDetail actual = UserDetailCreateConverter.userDetailConvertToEntity(user,
        userDetailCreateRequest);
    // 検証
    assertThat(actual.getFirstName()).isEqualTo("test");
    assertThat(actual.getLastName()).isEqualTo("sample");
    assertThat(actual.getFirstNameKana()).isEqualTo("ﾃｽﾄ");
    assertThat(actual.getLastNameKana()).isEqualTo("ｻﾝﾌﾟﾙ");
    assertThat(actual.getBirthday()).isEqualTo(LocalDate.of(2021, 1, 1));
    assertThat(actual.getMobilePhoneNumber()).isEqualTo("090-1234-5678");
    assertThat(actual.getPassword()).isEqualTo("Test@1234");
  }
}
