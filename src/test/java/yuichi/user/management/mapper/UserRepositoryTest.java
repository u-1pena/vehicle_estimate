package yuichi.user.management.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;

@MybatisTest
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Nested
  class ReadClass {

    @Test
    void ユーザーの全件検索がおこなえること() {
      List<User> actual = userRepository.findAllUsers();
      assertThat(actual.size()).isEqualTo(4);
    }

    @Test
    void ユーザー詳細の全件検索ができること() {
      List<UserDetail> actual = userRepository.findAllUserDetails();
      assertThat(actual.size()).isEqualTo(4);
    }

    @Test
    void ユーザー支払い情報の全件検索ができること() {
      List<UserPayment> acutal = userRepository.findAllUserPayments();
      assertThat(acutal.size()).isEqualTo(4);
    }

    @Test
    void 指定したidでユーザー情報を検索することができる() {
      Optional<User> actual = userRepository.findUserById(1);
      assertThat(actual.isPresent());
      assertThat(actual.get().getId()).isEqualTo(1);
      assertThat(actual.get().getAccount()).isEqualTo("ganmo");
      assertThat(actual.get().getEmail()).isEqualTo("shimaichi5973@gmail.com");
    }

    @Test
    void 指定したidでユーザー詳細情報を検索することができる() {
      Optional<UserDetail> actual = userRepository.findUserDetailById(1);
      assertThat(actual.isPresent());
      assertThat(actual.get().getId()).isEqualTo(1);
      assertThat(actual.get().getFirstName()).isEqualTo("yuichi");
      assertThat(actual.get().getLastName()).isEqualTo("shimada");
      assertThat(actual.get().getFirstNameKana()).isEqualTo("ﾕｳｲﾁ");
      assertThat(actual.get().getLastNameKana()).isEqualTo("ｼﾏﾀﾞ");
      assertThat(actual.get().getBirthday()).isEqualTo("1984-07-03");
      assertThat(actual.get().getMobilePhoneNumber()).isEqualTo("080-1379-0555");
      assertThat(actual.get().getPassword()).isEqualTo("Shimaichi@0703");
    }

    @Test
    void 指定したuserIdでユーザー支払い情報を検索することができる() {
      List<UserPayment> actual = userRepository.findUserPaymentsByUserId(1);
      assertThat(actual.size()).isEqualTo(1);
      assertThat(actual.get(0).getUserId()).isEqualTo(1);
      assertThat(actual.get(0).getCardNumber()).isEqualTo("4444123456789012");
      assertThat(actual.get(0).getCardBrand()).isEqualTo("visa");
      assertThat(actual.get(0).getCardHolder()).isEqualTo("YUICHI SHIMADA");
      assertThat(actual.get(0).getExpirationDate()).asString().isEqualTo("2028-01");
    }

    @Test
    void 指定したuserIdでユーザー支払い情報が複数あっても検索することができる() {
      List<UserPayment> actual = userRepository.findUserPaymentsByUserId(3);
      assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void アカウント名でユーザー情報を検索することができる() {
      List<User> actual = userRepository.findByAccountName("ganmo");
      assertThat(actual.size()).isEqualTo(1);
      assertThat(actual.get(0).getId()).isEqualTo(1);
      assertThat(actual.get(0).getAccount()).isEqualTo("ganmo");
      assertThat(actual.get(0).getEmail()).isEqualTo("shimaichi5973@gmail.com");
    }

    @Test
    void 読みがなでユーザー詳細情報を検索することができる() {
      List<UserDetail> actual = userRepository.findByFullNameKana("ﾕｳｲﾁ");
      assertThat(actual.size()).isEqualTo(1);
      assertThat(actual.get(0).getId()).isEqualTo(1);
      assertThat(actual.get(0).getFirstName()).isEqualTo("yuichi");
      assertThat(actual.get(0).getLastName()).isEqualTo("shimada");
      assertThat(actual.get(0).getFirstNameKana()).isEqualTo("ﾕｳｲﾁ");
      assertThat(actual.get(0).getLastNameKana()).isEqualTo("ｼﾏﾀﾞ");
      assertThat(actual.get(0).getBirthday()).isEqualTo("1984-07-03");
      assertThat(actual.get(0).getMobilePhoneNumber()).isEqualTo("080-1379-0555");
      assertThat(actual.get(0).getPassword()).isEqualTo("Shimaichi@0703");
    }

    @Test
    void 名前でユーザー詳細情報を検索することができる() {
      List<UserDetail> actual = userRepository.findByDetailName("yuichi");
      assertThat(actual.size()).isEqualTo(1);
      assertThat(actual.get(0).getId()).isEqualTo(1);
      assertThat(actual.get(0).getFirstName()).isEqualTo("yuichi");
      assertThat(actual.get(0).getLastName()).isEqualTo("shimada");
      assertThat(actual.get(0).getFirstNameKana()).isEqualTo("ﾕｳｲﾁ");
      assertThat(actual.get(0).getLastNameKana()).isEqualTo("ｼﾏﾀﾞ");
      assertThat(actual.get(0).getBirthday()).isEqualTo("1984-07-03");
      assertThat(actual.get(0).getMobilePhoneNumber()).isEqualTo("080-1379-0555");
      assertThat(actual.get(0).getPassword()).isEqualTo("Shimaichi@0703");
    }

    @Test
    void メールアドレスでユーザー情報を検索することができる() {
      List<User> actual = userRepository.findByEmail("shimaichi5973@gmail.com");
      assertThat(actual.size()).isEqualTo(1);
      assertThat(actual.get(0).getId()).isEqualTo(1);
      assertThat(actual.get(0).getAccount()).isEqualTo("ganmo");
      assertThat(actual.get(0).getEmail()).isEqualTo("shimaichi5973@gmail.com");
    }
  }
}
