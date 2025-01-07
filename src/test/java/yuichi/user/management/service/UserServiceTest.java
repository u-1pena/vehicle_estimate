package yuichi.user.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
import yuichi.user.management.helper.TestHelper;
import yuichi.user.management.mapper.UserRepository;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  TestHelper testHelper;

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void ユーザー情報を全件取得できること() {
      List<User> expectedUsers = testHelper.mockUsers();
      List<UserDetail> expectedDetailUsers = testHelper.mockUserDetails();
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments();
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
        assertUserInformation(actual, expectedUser, expectedUserDetail,
            userPayments);
      }
      verify(userRepository).findAllUsers();
      verify(userRepository).findAllUserDetails();
      verify(userRepository).findAllUserPayments();
    }

    @Test
    void 指定したidでユーザー情報を取得できること() {
      User expectedUser = testHelper.mockUsers().get(0);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == 1)
          .toList();
      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(1);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(1);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(1);

      List<UserInformationDto> actualList = userService.findUserInformationById(1);

      UserInformationDto actual = actualList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    @Test
    void 指定したアカウント名でユーザー情報を検索すること() {
      User expectedUser = testHelper.mockUsers().get(1);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(1);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
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
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    @Test
    void 指定した名字や名前でユーザー情報を検索すること() {
      User expectedUser = testHelper.mockUsers().get(0);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
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
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    @Test
    void 指定した読み仮名でユーザー情報を検索すること() {
      User expectedUser = testHelper.mockUsers().get(3);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(3);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
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
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    @Test
    void 指定したEメールでユーザー情報を検索すること() {
      User expectedUser = testHelper.mockUsers().get(0);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(0);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
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
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    //id:3のユーザーは支払い情報を複数持ったオブジェクトです
    @Test
    void 複数の支払い情報を持つユーザーを正しく取得できること() {
      User expectedUser = testHelper.mockUsers().get(2);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(2);
      List<UserPayment> expectedUserPayments = testHelper.mockUserPayments()
          .stream()
          .filter(userPayment -> userPayment.getUserId() == 3)
          .toList();
      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(3);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(3);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(3);

      List<UserInformationDto> userInfoList = userService.findUserInformationById(3);
      UserInformationDto actual = userInfoList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
    }

    //id:4のユーザーは支払い情報を持っていないオブジェクトです
    @Test
    void 支払い情報を持たないユーザーを正しく取得できること() {
      User expectedUser = testHelper.mockUsers().get(3);
      UserDetail expectedUserDetail = testHelper.mockUserDetails().get(3);
      List<UserPayment> expectedUserPayments = Collections.emptyList();

      doReturn(Optional.of(expectedUser)).when(userRepository).findUserById(4);
      doReturn(Optional.of(expectedUserDetail)).when(userRepository).findUserDetailById(4);
      doReturn(expectedUserPayments).when(userRepository).findUserPaymentsByUserId(4);

      List<UserInformationDto> userInfoList = userService.findUserInformationById(4);
      UserInformationDto actual = userInfoList.get(0);
      assertUserInformation(actual, expectedUser, expectedUserDetail,
          expectedUserPayments);
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
    void 存在しないアカウントで検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(userRepository).findByAccountName("unknownAccount");
      assertThat(userService.searchUsersByRequestParam("unknownAccount", "", "", ""))
          .isEmpty();
    }

    @Test
    void 存在しない名前で検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(userRepository).findByDetailName("unknownName");
      assertThat(userService.searchUsersByRequestParam("", "unknownName", "", ""))
          .isEmpty();
    }

    @Test
    void 存在しない読みがなで検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(userRepository).findByFullNameKana("ヲ");
      assertThat(userService.searchUsersByRequestParam("", "", "ヲ", ""))
          .isEmpty();
    }

    @Test
    void 存在しないEmailで検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(userRepository).findByEmail("unknown@test.jp");
      assertThat(userService.searchUsersByRequestParam("", "", "", "unknown@test.jp"))
          .isEmpty();
    }

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
}
