package yuichi.user.management.converter;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.helper.TestHelper;

class UserInformationConverterTest {

  TestHelper testHelper = new TestHelper();

  @Test
  void ユーザー情報をまとめて正常に変換できる() {
    User user = testHelper.usersMock().get(0);
    UserDetail userDetail = testHelper.userDetailsMock().get(0);
    List<UserPayment> userPayment = List.of(testHelper.userPaymentsMock().get(0));

    UserInformationDto actual = UserInformationConverter.convertToUserInformationDto(
        user,
        userDetail, userPayment);

    assertThat(actual.getUser()).isEqualTo(user);
    assertThat(actual.getUserDetail()).isEqualTo(userDetail);
    assertThat(actual.getUserPayment()).isEqualTo(userPayment);
  }

  @Test
  void ユーザーの支払い情報が複数ある場合に正常に変換できる() {
    User user = testHelper.usersMock().get(2);
    UserDetail userDetail = testHelper.userDetailsMock().get(2);
    List<UserPayment> userPayment = testHelper.userPaymentsMock()
        .stream()
        .filter(userPayment1 -> userPayment1.getUserId() == 3)
        .toList();

    UserInformationDto actual = UserInformationConverter.convertToUserInformationDto(
        user,
        userDetail, userPayment);

    assertThat(actual.getUser()).isEqualTo(user);
    assertThat(actual.getUserDetail()).isEqualTo(userDetail);
    assertThat(actual.getUserPayment()).isEqualTo(userPayment);
    assertThat(actual.getUserPayment().size()).isEqualTo(2);
  }

  @Test
  void ユーザーの支払い情報が空の場合に正常に変換できる() {
    User user = testHelper.usersMock().get(3);
    UserDetail userDetail = testHelper.userDetailsMock().get(3);
    List<UserPayment> userPayment = emptyList();

    UserInformationDto actual = UserInformationConverter.convertToUserInformationDto(
        user,
        userDetail, userPayment);

    assertThat(actual.getUser()).isEqualTo(user);
    assertThat(actual.getUserDetail()).isEqualTo(userDetail);
    assertThat(actual.getUserPayment()).isEqualTo(userPayment);
    assertThat(actual.getUserPayment().size()).isEqualTo(0);
  }
}
