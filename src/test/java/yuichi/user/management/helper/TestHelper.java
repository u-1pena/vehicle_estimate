package yuichi.user.management.helper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

public class TestHelper {

  /*
   * テスト用のユーザーデータを作成するクラス
   */
  public List<User> usersMock() {
    return List.of(
        new User(1, "ganmo", "shimaichi5973@gmail.com"),
        new User(2, "yurina", "kana1231@gmail.com"),
        new User(3, "jyun", "jyun0702@ymail.com"),
        new User(4, "momo", "momonga1103@docomo.ne.jp"));
  }

  /*
   * テスト用のユーザー詳細データを作成するクラス
   */
  public List<UserDetail> userDetailsMock() {
    return List.of(
        new UserDetail(1, "yuichi", "shimada", "ユウイチ", "テスト",
            LocalDate.of(1984, 7, 3), "080-1379-0555", "Shimaichi@0703"),
        new UserDetail(2, "kana", "nishida", "カナ", "ナカムラ",
            LocalDate.of(1993, 12, 31), "090-1234-5678", "Kana@1231"),
        new UserDetail(3, "yoshiyuki", "mine", "ヨシユキ", "ミネ",
            LocalDate.of(1984, 7, 2), "070-1234-5678", "Yoshiyuki@0702"),
        new UserDetail(4, "momoko", "tanaka", "モモコ", "タナカ",
            LocalDate.of(2000, 11, 3), "080-1111-6660", "momoKo@1212"));

  }

  /*
   * テスト用のユーザー支払いデータを作成するクラス
   */
  public List<UserPayment> userPaymentsMock() {
    return new ArrayList<>(List.of(
        new UserPayment(1, 1, "4444123456789012", "VISA", "YUICHI TEST",
            YearMonth.of(2028, 1)),
        new UserPayment(2, 2, "3530123456789012", "JCB", "KANA NAKAMURA",
            YearMonth.of(2029, 1)),
        new UserPayment(3, 3, "3412123412341234", "AmericanExpress", "YOSHIYUKI MINE",
            YearMonth.of(2025, 4)),
        new UserPayment(4, 3, "5112123412341234", "MasterCard", "YOSHIYUKI MINE",
            YearMonth.of(2027, 6))));
  }

  public User createUserMock() {
    User user = new User(userCreateRequestMock().getUserAccount(),
        userCreateRequestMock().getEmail());
    return user;
  }

  public UserCreateRequest userCreateRequestMock() {
    UserCreateRequest userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUserAccount("test");
    userCreateRequest.setEmail("test@example.ne.jp");
    return userCreateRequest;
  }

  public UserDetail createUserDetailMock(User user) {
    UserDetail userDetail = new UserDetail(user.getId(),
        userDetailCreateRequestMock().getFirstName(),
        userDetailCreateRequestMock().getLastName(),
        userDetailCreateRequestMock().getFirstNameKana(),
        userDetailCreateRequestMock().getLastNameKana(),
        LocalDate.parse(userDetailCreateRequestMock().getBirthday()),
        userDetailCreateRequestMock().getMobilePhoneNumber(),
        userDetailCreateRequestMock().getPassword());
    return userDetail;
  }

  public UserDetailCreateRequest userDetailCreateRequestMock() {
    UserDetailCreateRequest userDetailCreateRequest = new UserDetailCreateRequest();
    userDetailCreateRequest.setFirstName("test");
    userDetailCreateRequest.setLastName("test");
    userDetailCreateRequest.setFirstNameKana("テスト");
    userDetailCreateRequest.setLastNameKana("テスト");
    userDetailCreateRequest.setBirthday("2000-01-01");
    userDetailCreateRequest.setMobilePhoneNumber("090-1111-9999");
    userDetailCreateRequest.setPassword("Test@0101");
    return userDetailCreateRequest;
  }

  public UserPayment createUserPaymentMock(UserDetail userDetail) {
    UserPayment userPayment = new UserPayment(userDetail.getId(),
        userPaymentCreateRequestMock().getCardNumber(),
        userPaymentCreateRequestMock().getCardBrand(),
        userPaymentCreateRequestMock().getCardHolder(),
        YearMonth.parse(userPaymentCreateRequestMock().getExpirationDate()));
    return userPayment;
  }

  public UserPaymentCreateRequest userPaymentCreateRequestMock() {
    UserPaymentCreateRequest userPaymentCreateRequest = new UserPaymentCreateRequest();
    userPaymentCreateRequest.setCardNumber("4444123456789019");
    userPaymentCreateRequest.setCardBrand("VISA");
    userPaymentCreateRequest.setCardHolder("TEST USER");
    userPaymentCreateRequest.setExpirationDate("2028-01");
    return userPaymentCreateRequest;
  }
}
