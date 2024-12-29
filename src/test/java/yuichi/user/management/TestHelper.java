package yuichi.user.management;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.test.web.servlet.MvcResult;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

public class TestHelper {

  /*
   * テスト用のユーザーデータを作成するクラス
   */
  public List<User> mockUsers() {
    return List.of(
        new User(1, "ganmo", "shimaichi5973@gmail.com"),
        new User(2, "yurina", "kana1231@gmail.com"),
        new User(3, "jyun", "jyun0702@aol.com"),
        new User(4, "momo", "momonga1103@docomo.ne.jp"));
  }

  /*
   * テスト用のユーザー詳細データを作成するクラス
   */
  public List<UserDetail> mockUserDetails() {
    return List.of(
        new UserDetail(1, "yuichi", "shimada", "ﾕｳｲﾁ", "ｼﾏﾀﾞ",
            LocalDate.of(1984, 7, 3), "080-1379-0555", "Shimaichi@5973"),
        new UserDetail(2, "kana", "nishida", "ｶﾅ", "ﾆｼﾀﾞ",
            LocalDate.of(1993, 12, 31), "080-1234-5678", "Kana@1231"),
        new UserDetail(3, "yoshiyuki", "mine", "ﾖｼﾕｷ", "ﾐﾈ",
            LocalDate.of(1984, 7, 2), "090-1111-2222", "Jyun@0703"),
        new UserDetail(4, "momoko", "tanaka", "ﾓﾓｺ", "ﾀﾅｶ",
            LocalDate.of(2000, 11, 3), "080-1111-6660", "momoKo@1212"));

  }

  /*
   * テスト用のユーザー支払いデータを作成するクラス
   */
  public List<UserPayment> mockUserPayments() {
    return new ArrayList<>(List.of(
        new UserPayment(1, 1, "1234567890123456", "VISA", "YUICHI SHIMADA",
            YearMonth.of(2028, 1)),
        new UserPayment(2, 2, "9876543210987654", "MASTER", "KANA NISHIDA",
            YearMonth.of(2029, 1)),
        new UserPayment(3, 3, "3333333333333333", "JCB", "YOSHIYUKI MINE",
            YearMonth.of(2028, 1)),
        new UserPayment(4, 3, "4444444444444444", "AMEX", "YOSHIYUKI MINE",
            YearMonth.of(2029, 1))));
  }

  public void assertServiceUserInformation(UserInformationDto actual, User expectedUser,
      UserDetail expectedUserDetail,
      List<UserPayment> expectedUserPayments) {
    assertThat(actual.getUser().getId()).isEqualTo(expectedUser.getId());
    assertThat(actual.getUser().getAccount()).isEqualTo(expectedUser.getAccount());
    assertThat(actual.getUser().getEmail()).isEqualTo(expectedUser.getEmail());

    assertThat(actual.getUserDetail().getId()).isEqualTo(expectedUserDetail.getId());
    assertThat(actual.getUserDetail().getFirstName()).isEqualTo(
        expectedUserDetail.getFirstName());
    assertThat(actual.getUserDetail().getLastName()).isEqualTo(expectedUserDetail.getLastName());
    assertThat(actual.getUserDetail().getFirstNameKana()).isEqualTo(
        expectedUserDetail.getFirstNameKana());
    assertThat(actual.getUserDetail().getLastNameKana()).isEqualTo(
        expectedUserDetail.getLastNameKana());
    assertThat(actual.getUserDetail().getBirthday()).isEqualTo(expectedUserDetail.getBirthday());
    assertThat(actual.getUserDetail().getMobilePhoneNumber()).isEqualTo(
        expectedUserDetail.getMobilePhoneNumber());
    assertThat(actual.getUserDetail().getPassword()).isEqualTo(expectedUserDetail.getPassword());
    assertThat(actual.getUserPayment()).isNotNull();
    assertThat(actual.getUserPayment()).hasSize(expectedUserPayments.size());
    if (expectedUserPayments.isEmpty()) {
      assertThat(actual.getUserPayment()).isEmpty();
      return;
    }
    for (int i = 0; i < expectedUserPayments.size(); i++) {
      UserPayment actualPayment = actual.getUserPayment().get(i);
      UserPayment expectedPayment = expectedUserPayments.get(i);
      assertThat(actualPayment.getId()).isEqualTo(expectedPayment.getId());
      assertThat(actualPayment.getUserId()).isEqualTo(expectedPayment.getUserId());
      assertThat(actualPayment.getCardNumber()).isEqualTo(expectedPayment.getCardNumber());
      assertThat(actualPayment.getCardBrand()).isEqualTo(expectedPayment.getCardBrand());
      assertThat(actualPayment.getCardHolder()).isEqualTo(expectedPayment.getCardHolder());
      assertThat(actualPayment.getExpirationDate()).isEqualTo(
          expectedPayment.getExpirationDate());
    }
  }

  public void assertControllerUserResponses(MvcResult result, List<UserInformationDto> expectedList)
      throws Exception {
    String responseBody = result.getResponse().getContentAsString();
    System.out.println("Response Body: " + result.getResponse().getContentAsString());

    // 空リストの場合の対応
    if (expectedList.isEmpty()) {
      assertThat(responseBody).isEqualTo("[]"); // 空のJSONリストであることを検証
      return;
    }

    for (UserInformationDto expected : expectedList) {
      assertThat(responseBody).contains("\"id\":" + expected.getUser().getId());
      assertThat(responseBody).contains(
          "\"account\":\"" + expected.getUser().getAccount() + "\"");
      assertThat(responseBody).contains("\"email\":\"" + expected.getUser().getEmail() + "\"");
      assertThat(responseBody).contains(
          "\"firstName\":\"" + expected.getUserDetail().getFirstName() + "\"");
      assertThat(responseBody).contains(
          "\"lastName\":\"" + expected.getUserDetail().getLastName() + "\"");
      assertThat(responseBody).contains(
          "\"firstNameKana\":\"" + expected.getUserDetail().getFirstNameKana() + "\"");
      assertThat(responseBody).contains(
          "\"lastNameKana\":\"" + expected.getUserDetail().getLastNameKana() + "\"");
      assertThat(responseBody).contains(
          "\"birthday\":\"" + expected.getUserDetail().getBirthday() + "\"");
      assertThat(responseBody).contains(
          "\"mobilePhoneNumber\":\"" + expected.getUserDetail().getMobilePhoneNumber() + "\"");
      assertThat(responseBody).contains(
          "\"password\":\"" + expected.getUserDetail().getPassword() + "\"");

      for (int j = 0; j < expected.getUserPayment().size(); j++) {
        assertThat(responseBody).contains("\"id\":" + expected.getUserPayment().get(j).getId());
        assertThat(responseBody).contains(
            "\"userId\":" + expected.getUserPayment().get(j).getUserId());
        assertThat(responseBody).contains(
            "\"cardNumber\":\"" + expected.getUserPayment().get(j).getCardNumber() + "\"");
        assertThat(responseBody).contains(
            "\"cardBrand\":\"" + expected.getUserPayment().get(j).getCardBrand() + "\"");
        assertThat(responseBody).contains(
            "\"cardHolder\":\"" + expected.getUserPayment().get(j).getCardHolder() + "\"");
        assertThat(responseBody).contains(
            "\"expirationDate\":\"" + expected.getUserPayment().get(j).getExpirationDate()
                + "\"");
      }
    }
  }
}



