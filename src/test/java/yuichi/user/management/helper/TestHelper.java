package yuichi.user.management.helper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
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
}
