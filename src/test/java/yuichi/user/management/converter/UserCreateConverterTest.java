package yuichi.user.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.entity.User;

class UserCreateConverterTest {


  @Test
  void UserCreateRequestをUserに変換できること() {
    // 準備
    UserCreateRequest userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUserAccount("test");
    userCreateRequest.setEmail("test@example.co.jp");
    // 実行
    User actual = UserCreateConverter.userConvertToEntity(userCreateRequest);
    // 検証
    assertThat(actual.getUserAccount()).isEqualTo("test");
    assertThat(actual.getEmail()).isEqualTo("test@example.co.jp");
  }
}
