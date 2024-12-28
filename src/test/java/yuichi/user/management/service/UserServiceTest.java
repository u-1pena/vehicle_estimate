package yuichi.user.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yuichi.user.management.controller.exception.UserNotFoundException;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.mapper.UserRepository;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  @Nested
  class ReadClass {

    @Test
    void ユーザー情報を全件取得できること() {
      List<User> expectedUsers = mockUsers();
      List<UserDetail> expectedDetailUsers = mockUserDetails();
      List<UserPayment> expectedUserPayments = mockUserPayments();
      doReturn(expectedUsers).when(userRepository).findAllUsers();
      doReturn(expectedDetailUsers).when(userRepository).findAllUserDetails();
      doReturn(expectedUserPayments).when(userRepository).findAllUserPayments();

      List<UserInformationDto> actualList = userService.findAll();

      assertThat(actualList).hasSize(expectedUsers.size());
      for (int i = 0; i < expectedUsers.size(); i++) {

        UserInformationDto actual = actualList.get(i);
        User expectedUser = expectedUsers.get(i);
        UserDetail expectedUserDetail = expectedDetailUsers.get(i);

        List<UserPayment> userPayments = expectedUserPayments.stream()
            .filter(payment -> payment.getUserId() == expectedUser.getId())
            .toList();
        assertUserInformation(actual, expectedUser, expectedUserDetail, userPayments);
      }
      verify(userRepository).findAllUsers();
      verify(userRepository).findAllUserDetails();
      verify(userRepository).findAllUserPayments();
    }

    @Test
    void 指定したidでユーザー情報を取得できること() {
      User expectedUser = mockUsers().get(0);
      UserDetail expectedUserDetail = mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == 1)
          .toList();
      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(1);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(1);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(1);

      List<UserInformationDto> actualList = userService.findUserInformationById(1);

      UserInformationDto actual = actualList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    @Test
    void 指定したアカウント名でユーザー情報を検索すること() {
      User expectedUser = mockUsers().get(1);
      UserDetail expectedUserDetail = mockUserDetails().get(1);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == expectedUser.getId())
          .toList();
      doReturn(List.of(expectedUser)).when(userRepository).findByAccountName("yurina");
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUser.getId());
      doReturn(expectedUserPayments).when(userRepository)
          .findUserPaymentsByUserId(expectedUser.getId());

      List<UserInformationDto> actualList = userService.searchUsersByRequestParam("yurina", "", "",
          "");
      UserInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    @Test
    void 指定した名字や名前でユーザー情報を検索すること() {
      User expectedUser = mockUsers().get(0);
      UserDetail expectedUserDetail = mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == expectedUser.getId())
          .toList();
      doReturn(List.of(expectedUserDetail)).when(userRepository).findByDetailName("yuichi");
      doReturn(Optional.of(expectedUser)).when(userRepository)
          .findUserById(expectedUserDetail.getId());
      doReturn(expectedUserPayments).when(userRepository)
          .findUserPaymentsByUserId(expectedUserDetail.getId());

      List<UserInformationDto> actualList = userService.searchUsersByRequestParam("", "yuichi", "",
          "");
      UserInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    @Test
    void 指定した読み仮名でユーザー情報を検索すること() {
      User expectedUser = mockUsers().get(3);
      UserDetail expectedUserDetail = mockUserDetails().get(3);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == expectedUser.getId())
          .toList();
      doReturn(List.of(expectedUserDetail)).when(userRepository).findByFullNameKana("momo");
      doReturn(Optional.of(expectedUser)).when(userRepository)
          .findUserById(expectedUserDetail.getId());
      doReturn(expectedUserPayments).when(userRepository)
          .findUserPaymentsByUserId(expectedUserDetail.getId());

      List<UserInformationDto> actualList = userService.searchUsersByRequestParam("", "", "momo",
          "");
      UserInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    @Test
    void 指定したEメールでユーザー情報を検索すること() {
      User expectedUser = mockUsers().get(0);
      UserDetail expectedUserDetail = mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == expectedUser.getId())
          .toList();
      doReturn(List.of(expectedUser)).when(userRepository).findByEmail("shimaichi5973@gmail.com");
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUserDetail.getId());
      doReturn(expectedUserPayments).when(userRepository)
          .findUserPaymentsByUserId(expectedUser.getId());

      List<UserInformationDto> actualList = userService.searchUsersByRequestParam("",
          "", "",
          "shimaichi5973@gmail.com");
      UserInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }


    //id:3のユーザーは支払い情報を複数持ったオブジェクトです
    @Test
    void 複数の支払い情報を持つユーザーを正しく取得できること() {
      User expectedUser = mockUsers().get(2);
      UserDetail expectedUserDetail = mockUserDetails().get(2);
      List<UserPayment> expectedUserPayments = mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == 3)
          .toList();
      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(3);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(3);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(3);

      List<UserInformationDto> userInfoList = userService.findUserInformationById(3);
      UserInformationDto actual = userInfoList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    //id:4のユーザーは支払い情報を持っていないオブジェクトです
    @Test
    void 支払い情報を持たないユーザーを正しく取得できること() {
      User expectedUser = mockUsers().get(3);
      UserDetail expectedUserDetail = mockUserDetails().get(3);
      List<UserPayment> expectedUserPayments = Collections.emptyList();

      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(4);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(4);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(4);

      List<UserInformationDto> userInfoList = userService.findUserInformationById(4);
      UserInformationDto actual = userInfoList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail, expectedUserPayments);
    }

    @Test
    void 存在しないIDでユーザー検索をしたとき存在しないことを知らせること() {
      doReturn(Optional.empty()).when(userRepository).findUserById(0);
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.findUserInformationById(0);
      });
      assertEquals("user not found with id: 0", exception.getMessage());
    }

    @Test
    void 存在しないアカウントで検索したとき存在しないことを知らせること() {
      doReturn(Collections.emptyList()).when(userRepository).findByAccountName("unknownAccount");
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.searchUsersByRequestParam("unknownAccount", "", "", "");
      });
      assertEquals("user not found with account: unknownAccount", exception.getMessage());
    }

    @Test
    void 存在しない名前で検索したとき存在しないことを知らせること() {
      doReturn(Collections.emptyList()).when(userRepository).findByDetailName("unknownName");
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.searchUsersByRequestParam("", "unknownName", "", "");
      });
      assertEquals("user not found with name: unknownName", exception.getMessage());
    }

    @Test
    void 存在しない読みがなで検索したとき存在しないことを知らせること() {
      doReturn(Collections.emptyList()).when(userRepository).findByFullNameKana("ヲ");
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.searchUsersByRequestParam("", "", "ヲ", "");
      });
      assertEquals("user not found with name: ヲ", exception.getMessage());
    }

    @Test
    void 存在しないEmailで検索したとき存在しないことを知らせること() {
      doReturn(Collections.emptyList()).when(userRepository).findByEmail("unknown@test.jp");
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.searchUsersByRequestParam("", "", "", "unknown@test.jp");
      });
      assertEquals("user not found with email: unknown@test.jp", exception.getMessage());
    }

    //testロジックをメソッド化
    private void assertUserInformation(UserInformationDto actual, User expectedUser,
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
  }


  private List<User> mockUsers() {
    return List.of(
        new User(1, "ganmo", "shimaichi5973@gmail.com"),
        new User(2, "yurina", "kana1231@gmail.com"),
        new User(3, "jyun", "jyun0702@aol.com"),
        new User(4, "momo", "momonga1103@docomo.ne.jp"));
  }

  private List<UserDetail> mockUserDetails() {
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

  private List<UserPayment> mockUserPayments() {
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
