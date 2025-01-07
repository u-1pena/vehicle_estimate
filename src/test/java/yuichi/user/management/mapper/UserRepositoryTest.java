package yuichi.user.management.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.helper.TestHelper;

@MybatisTest
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  TestHelper testHelper;

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void ユーザーの全件検索がおこなえること() {
      List<User> actual = userRepository.findAllUsers();
      List<User> expected = testHelper.mockUsers();
      assertThat(actual).isEqualTo(expected);
      assertThat(actual.size()).isEqualTo(4);

    }

    @Test
    void ユーザー詳細の全件検索ができること() {
      List<UserDetail> actual = userRepository.findAllUserDetails();
      List<UserDetail> expected = testHelper.mockUserDetails();
      assertThat(actual).isEqualTo(expected);
      assertThat(actual.size()).isEqualTo(4);
    }

    @Test
    void ユーザー支払い情報の全件検索ができること() {
      List<UserPayment> actual = userRepository.findAllUserPayments();
      List<UserPayment> expected = testHelper.mockUserPayments();
      assertThat(actual).isEqualTo(expected);
      assertThat(actual.size()).isEqualTo(4);
    }

    @Test
    void 指定したidでユーザー情報を検索することができる() {
      Optional<User> actual = userRepository.findUserById(1);
      User expected = testHelper.mockUsers().get(0);
      assertThat(actual).hasValue(expected);
    }

    @Test
    void 指定したidでユーザー詳細情報を検索することができる() {
      Optional<UserDetail> actual = userRepository.findUserDetailById(1);
      UserDetail expected = testHelper.mockUserDetails().get(0);
      assertThat(actual).hasValue(expected);
    }

    @Test
    void 指定したuserIdでユーザー支払い情報を検索することができる() {
      List<UserPayment> actual = userRepository.findUserPaymentsByUserId(1);
      UserPayment expected = testHelper.mockUserPayments().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 指定したuserIdでユーザー支払い情報が複数あっても検索することができる() {
      List<UserPayment> actual = userRepository.findUserPaymentsByUserId(3);
      List<UserPayment> expected = testHelper.mockUserPayments().stream()
          .filter(userPayment -> userPayment.getUserId() == 3)
          .toList();
      assertThat(actual).isEqualTo(expected);
      assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void アカウント名でユーザー情報を検索することができる() {
      List<User> actual = userRepository.findByAccountName("ganmo");
      User expected = testHelper.mockUsers().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 読みがなでユーザー詳細情報を検索することができる() {
      List<UserDetail> actual = userRepository.findByFullNameKana("ﾕｳｲﾁ");
      UserDetail expected = testHelper.mockUserDetails().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void 名前でユーザー詳細情報を検索することができる() {
      List<UserDetail> actual = userRepository.findByDetailName("yuichi");
      UserDetail expected = testHelper.mockUserDetails().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void メールアドレスでユーザー情報を検索することができる() {
      List<User> actual = userRepository.findByEmail("shimaichi5973@gmail.com");
      User expected = testHelper.mockUsers().get(0);
      assertThat(actual).contains(expected);
      assertThat(actual.size()).isEqualTo(1);
    }
  }
}
