package yuichi.user.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import yuichi.user.management.controller.exception.PaymentExpirationInvalidException;
import yuichi.user.management.controller.exception.UserAlreadyExistsException;
import yuichi.user.management.controller.exception.UserDetailAlreadyExistsException;
import yuichi.user.management.controller.exception.UserNotFoundException;
import yuichi.user.management.controller.exception.UserPaymentAlreadyExistsException;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
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
      List<User> expectedUsers = testHelper.usersMock();
      List<UserDetail> expectedDetailUsers = testHelper.userDetailsMock();
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock();
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
      User expectedUser = testHelper.usersMock().get(0);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(1);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(1);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(0);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(3);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(3);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(0);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(2);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(2);
      List<UserPayment> expectedUserPayments = testHelper.userPaymentsMock()
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
      User expectedUser = testHelper.usersMock().get(3);
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(3);
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

  @Nested
  class CreateClass {

    @Test
    public void Emailが存在しない場合にユーザーを登録できること() {

      //準備
      UserCreateRequest userCreateRequest = testHelper.userCreateRequestMock();
      User expectedUser = testHelper.createUserMock();

      //モックの振る舞いを設定
      when(userRepository.checkAlreadyExistByEmail(userCreateRequest.getEmail())).thenReturn(
          Optional.empty());
      doNothing().when(userRepository).createUser(expectedUser);

      //実行
      User actual = userService.registerUser(userCreateRequest);

      //検証
      assertThat(actual).isEqualTo(expectedUser);
      assertThat(actual.getAccount()).isEqualTo(expectedUser.getAccount());
      assertThat(actual.getEmail()).isEqualTo(expectedUser.getEmail());
      verify(userRepository, times(1)).createUser(expectedUser);
      verify(userRepository, times(1)).checkAlreadyExistByEmail(userCreateRequest.getEmail());
    }

    @Test
    void Eメールがすでに登録されていた場合エラーが発生すること() {
      //準備
      UserCreateRequest userCreateRequest = testHelper.userCreateRequestMock();
      User expectedUser = testHelper.createUserMock();

      //モックの振る舞いを設定
      when(userRepository.checkAlreadyExistByEmail(userCreateRequest.getEmail())).thenReturn(
          Optional.of(expectedUser));

      //実行
      UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
        userService.registerUser(userCreateRequest);
      });

      //検証
      assertEquals("User already exists with email: " + (expectedUser.getEmail()),
          exception.getMessage());
      verify(userRepository, times(1)).checkAlreadyExistByEmail(userCreateRequest.getEmail());
    }

    @Test
    void ユーザー詳細が登録できること() {
      //準備
      User expecteduser = testHelper.usersMock().get(0);
      UserDetailCreateRequest userDetailCreateRequest = testHelper.userDetailCreateRequestMock();
      UserDetail expectedUserDetail = testHelper.createUserDetailMock(expecteduser);

      //モックの振る舞いを設定
      doReturn(Optional.of(expecteduser)).when(userRepository).findUserById(expecteduser.getId());
      doReturn(Optional.empty()).when(userRepository).findUserDetailById(expecteduser.getId());
      when(userRepository.CheckAlreadyExistByMobilePhoneNumber(
          expectedUserDetail.getMobilePhoneNumber())).thenReturn(Optional.empty());
      doNothing().when(userRepository).createUserDetail(expectedUserDetail);

      //実行
      UserDetail actual = userService.registerUserDetail(expecteduser.getId(),
          userDetailCreateRequest);

      //検証
      assertUserDetail(actual, expectedUserDetail);
      verify(userRepository).findUserById(expecteduser.getId());
      verify(userRepository).findUserDetailById(expecteduser.getId());
      verify(userRepository).CheckAlreadyExistByMobilePhoneNumber(
          expectedUserDetail.getMobilePhoneNumber());
      verify(userRepository).createUserDetail(expectedUserDetail);
    }

    @Test
    void ユーザー登録がされていないIDでユーザー詳細を登録しようとするとエラーが発生すること() {
      UserDetailCreateRequest userDetailCreateRequest = testHelper.userDetailCreateRequestMock();

      //モックの振る舞いを設定
      doReturn(Optional.empty()).when(userRepository).findUserById(0);

      //実行
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.registerUserDetail(0, userDetailCreateRequest);
      });

      //検証
      assertEquals("user not found with id: 0", exception.getMessage());
      verify(userRepository, times(1)).findUserById(0);
    }

    @Test
    void 携帯番号が重複している場合登録ができずエラーが発生すること() {

      //準備
      User expecteduser = testHelper.usersMock().get(0);
      UserDetailCreateRequest userDetailCreateRequest = testHelper.userDetailCreateRequestMock();
      UserDetail expectedUserDetail = testHelper.createUserDetailMock(expecteduser);

      //モックの振る舞いを設定
      doReturn(Optional.of(expecteduser)).when(userRepository).findUserById(expecteduser.getId());
      doReturn(Optional.empty()).when(userRepository).findUserDetailById(expecteduser.getId());
      when(userRepository.CheckAlreadyExistByMobilePhoneNumber(
          expectedUserDetail.getMobilePhoneNumber())).thenReturn(Optional.of(expectedUserDetail));

      //実行
      UserDetailAlreadyExistsException exception = assertThrows(
          UserDetailAlreadyExistsException.class, () -> {
            userService.registerUserDetail(expecteduser.getId(), userDetailCreateRequest);
          });

      //検証
      assertEquals("UserDetail already exists with mobilePhoneNumber: "
              + expectedUserDetail.getMobilePhoneNumber(),
          exception.getMessage());
      verify(userRepository, times(1)).findUserById(expecteduser.getId());
      verify(userRepository, times(1)).findUserDetailById(expecteduser.getId());
      verify(userRepository, times(1)).CheckAlreadyExistByMobilePhoneNumber(
          expectedUserDetail.getMobilePhoneNumber());
    }

    @Test
    void ユーザー詳細を登録する際に生年月日が未来の日付だった場合エラーが発生すること() {
      //準備
      User expecteduser = testHelper.usersMock().get(0);
      UserDetailCreateRequest userDetailCreateRequest = testHelper.userDetailCreateRequestMock();
      userDetailCreateRequest.setBirthday("2028-01-01");
      UserDetail expectedUserDetail = testHelper.createUserDetailMock(expecteduser);

      //モックの振る舞いを設定
      doReturn(Optional.of(expecteduser)).when(userRepository).findUserById(expecteduser.getId());
      doReturn(Optional.empty()).when(userRepository).findUserDetailById(expecteduser.getId());

      //実行
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class, () -> {
            userService.registerUserDetail(expecteduser.getId(), userDetailCreateRequest);
          });

      //検証
      assertEquals("Birthday is invalid", exception.getMessage());
      verify(userRepository, times(1)).findUserById(expecteduser.getId());
    }

    @Test
    void ユーザー詳細がすでに登録されていたIDで登録しようとするとエラーが発生すること() {
      //準備
      User expecteduser = testHelper.usersMock().get(0);
      UserDetailCreateRequest userDetailCreateRequest = testHelper.userDetailCreateRequestMock();
      UserDetail expectedUserDetail = testHelper.createUserDetailMock(expecteduser);

      //モックの振る舞いを設定
      doReturn(Optional.of(expecteduser)).when(userRepository).findUserById(expecteduser.getId());
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expecteduser.getId());

      //実行
      UserDetailAlreadyExistsException exception = assertThrows(
          UserDetailAlreadyExistsException.class, () -> {
            userService.registerUserDetail(expecteduser.getId(), userDetailCreateRequest);
          });

      //検証
      assertEquals("userDetail already exist with id: " + expectedUserDetail.getId(),
          exception.getMessage());
      verify(userRepository, times(1)).findUserById(expecteduser.getId());
    }

    //UserDetailオブジェクトのフィールド値を検証するメソッド
    private void assertUserDetail(UserDetail actual, UserDetail expectedUserDetail) {
      assertThat(actual.getId()).isEqualTo(expectedUserDetail.getId());
      assertThat(actual.getFirstName()).isEqualTo(expectedUserDetail.getFirstName());
      assertThat(actual.getLastName()).isEqualTo(expectedUserDetail.getLastName());
      assertThat(actual.getFirstNameKana()).isEqualTo(expectedUserDetail.getFirstNameKana());
      assertThat(actual.getLastNameKana()).isEqualTo(expectedUserDetail.getLastNameKana());
      assertThat(actual.getBirthday()).isEqualTo(expectedUserDetail.getBirthday());
      assertThat(actual.getMobilePhoneNumber()).isEqualTo(
          expectedUserDetail.getMobilePhoneNumber());
      assertThat(actual.getPassword()).isEqualTo(expectedUserDetail.getPassword());
    }

    @Test
    void クレジット番号が重複していなければユーザーの支払い情報が登録できること() {
      //準備
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      UserPaymentCreateRequest userPaymentCreateRequest = testHelper.userPaymentCreateRequestMock();
      UserPayment expectedUserPayment = testHelper.createUserPaymentMock(expectedUserDetail);

//モックの振る舞いを設定
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUserDetail.getId());
      when(userRepository.checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber())).thenReturn(
          Optional.empty());
      doNothing().when(userRepository).createUserPayment(expectedUserPayment);

      //実行
      UserPayment actual = userService.registerUserPayment(expectedUserDetail.getId(),
          userPaymentCreateRequest);

      //検証
      assertUserPayment(actual, expectedUserPayment);
      verify(userRepository, times(1)).findUserDetailById(expectedUserDetail.getId());
      verify(userRepository, times(1)).checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber());
      verify(userRepository).createUserPayment(expectedUserPayment);
    }

    @Test
    void ユーザー詳細登録のないIDで支払い情報を登録しようとするとエラーが発生すること() {
      //準備
      UserPaymentCreateRequest userPaymentCreateRequest = testHelper.userPaymentCreateRequestMock();

      //モックの振る舞いを設定
      doReturn(Optional.empty()).when(userRepository).findUserDetailById(0);

      //実行
      UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
        userService.registerUserPayment(0, userPaymentCreateRequest);
      });

      //検証
      assertEquals("user not found with id: 0", exception.getMessage());
      verify(userRepository, times(1)).findUserDetailById(0);
    }

    @Test
    void 既に支払い情報が登録されていてもクレジット番号が重複していなければ登録できること() {
      //準備
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      UserPaymentCreateRequest userPaymentCreateRequest = testHelper.userPaymentCreateRequestMock();
      UserPayment expectedUserPayment = testHelper.createUserPaymentMock(expectedUserDetail);

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUserDetail.getId());
      when(userRepository.checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber())).thenReturn(
          Optional.empty());
      doNothing().when(userRepository).createUserPayment(expectedUserPayment);

      //実行
      UserPayment actual = userService.registerUserPayment(expectedUserDetail.getId(),
          userPaymentCreateRequest);

      //検証
      assertUserPayment(actual, expectedUserPayment);
      verify(userRepository, times(1)).findUserDetailById(expectedUserDetail.getId());
      verify(userRepository, times(1)).checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber());
      verify(userRepository).createUserPayment(expectedUserPayment);
    }

    @Test
    void クレジット番号が重複している場合エラーが発生すること() {
      //準備
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      UserPaymentCreateRequest userPaymentCreateRequest = testHelper.userPaymentCreateRequestMock();
      UserPayment expectedUserPayment = testHelper.createUserPaymentMock(expectedUserDetail);

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUserDetail.getId());
      when(userRepository.checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber())).thenReturn(
          Optional.of(expectedUserPayment));

      //実行
      UserPaymentAlreadyExistsException exception = assertThrows(
          UserPaymentAlreadyExistsException.class, () -> {
            userService.registerUserPayment(expectedUserDetail.getId(), userPaymentCreateRequest);
          });

      //検証
      assertEquals("UserPayment already exists with cardNumber: "
              + expectedUserPayment.getCardNumber(),
          exception.getMessage());
    }

    @Test
    void クレジット番号が4から始まる場合ブランドVISAを取得すること() {
      String cardNumber = "4444123456789012";
      String expectedCardBrand = "VISA";

      String actual = userService.identifyCardBrand(cardNumber);

      assertThat(actual).isEqualTo(expectedCardBrand);
    }

    @Test
    void クレジットカード番号が5から始まり51から55の間か2221から2720の間ならブランドMasterCardを取得すること() {

      /*
      MasterCard識別番号　51-55,2221-2720
       */
      //準備
      String cardNumber;
      String expectedCardBrand;

      //51-55をMasterCardとして認識する
      for (int i = 51; i <= 55; i++) {
        cardNumber = i + "11234123412345";
        expectedCardBrand = "MasterCard";
        //実行
        String actual = userService.identifyCardBrand(cardNumber);
        //検証
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
      //2221-2720をMasterCardとして認識する
      for (int i = 2221; i <= 2720; i++) {
        cardNumber = i + "123412341234";
        expectedCardBrand = "MasterCard";
        //実行
        String actual = userService.identifyCardBrand(cardNumber);
        //検証
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
    }

    @Test
    void クレジットカード番号が62で始まる場合ブランドUnionPayを取得すること() {
      /*
      UnionPay識別番号　62
       */
      String cardNumber = "6212341234123412";
      String expectedCardBrand = "UnionPay";

      String actual = userService.identifyCardBrand(cardNumber);

      assertThat(actual).isEqualTo(expectedCardBrand);
    }

    @Test
    void クレジットカード番号が34もしくは37から始まる場合ブランドAmericanExpressを取得すること() {
      /*
      AmericanExpress識別番号　34,37
       */
      String cardNumber;
      String expectedCardBrand;
      //for文に+3をしているのは34,37のみのため
      for (int i = 34; i <= 37; i += 3) {
        cardNumber = i + "1234567812345";
        expectedCardBrand = "AmericanExpress";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
    }


    @Test
    void クレジットカード番号が指定の識別番号から始まる場合ブランドDiscoverを取得すること() {
      /*
      Discover識別番号　
      60110,60112-60114
      601174-601179,601186-601199
      644-649
      65
       */
      String cardNumber;
      String expectedCardBrand;

      //60110,60112-60114と644-650の間の番号をDiscoverとして認識する。
      int[] discoverStartNumbers = {60110, 60112, 60113, 60114, 64400, 64500, 64600, 64700, 64800,
          64900, 65000};
      for (int i : discoverStartNumbers) {
        cardNumber = i + "12341234123";
        expectedCardBrand = "Discover";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }

      //601174-601179,601186-601199をDiscoverとして認識する
      for (int i = 601174; i <= 601179; i++) {
        cardNumber = i + "1234567890";
        expectedCardBrand = "Discover";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }

      //601186-601199をDiscoverとして認識する
      for (int i = 601186; i <= 601199; i++) {
        cardNumber = i + "1234567890";
        expectedCardBrand = "Discover";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
    }

    @Test
    void クレジットカード番号が300から305から始まる場合ブランドDinersClubを取得すること() {
      /*
      Diners Club識別番号
      300-305
      3095
      36、38-39

       */
      String cardNumber;
      String expectedCardBrand;
      //300-305をDinersとして認識する
      for (int i = 300; i <= 305; i++) {
        cardNumber = i + "1123412341234";
        expectedCardBrand = "Diners";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
      //36,38,39をDinersとして認識する
      int[] dinersClubStartNumbers = {36, 38, 39};
      for (int i : dinersClubStartNumbers) {
        cardNumber = i + "12341234123412";
        expectedCardBrand = "Diners";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
      //3095をDinersとして認識する
      cardNumber = "3095" + "123412341234";
      expectedCardBrand = "Diners";
      String actual = userService.identifyCardBrand(cardNumber);
      assertThat(actual).isEqualTo(expectedCardBrand);
    }

    //JCBは3528から3589
    @Test
    void クレジットカード番号が3528から3589の間ならブランドJCBを取得すること() {
      /*
      JCB識別番号　3528-3589
       */
      String cardNumber;
      String expectedCardBrand;
      for (int i = 3528; i <= 3589; i++) {
        cardNumber = i + "123412341234";
        expectedCardBrand = "JCB";
        String actual = userService.identifyCardBrand(cardNumber);
        assertThat(actual).isEqualTo(expectedCardBrand);
      }
    }

    @Test
    void クレジット番号がブランドの条件に当てはまらない場合エラーをかえすこと() {
      String cardNumber = "1234123412341234";
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        userService.identifyCardBrand(cardNumber);
      });
      assertEquals("Unknown card brand", exception.getMessage());
    }

    @Test
    void クレジットの有効期限が過去の場合エラーをかえすこと() {
      //準備
      UserDetail expectedUserDetail = testHelper.userDetailsMock().get(0);
      UserPaymentCreateRequest userPaymentCreateRequest = testHelper.userPaymentCreateRequestMock();
      userPaymentCreateRequest.setExpirationDate("2021-01");
      UserPayment expectedUserPayment = testHelper.createUserPaymentMock(expectedUserDetail);

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedUserDetail)).when(userRepository)
          .findUserDetailById(expectedUserDetail.getId());
      when(userRepository.checkAlreadyExistByCardNumber(
          userPaymentCreateRequest.getCardNumber())).thenReturn(
          Optional.empty());

      //実行
      PaymentExpirationInvalidException exception = assertThrows(
          PaymentExpirationInvalidException.class, () -> {
            userService.registerUserPayment(expectedUserDetail.getId(), userPaymentCreateRequest);
          });

      //検証
      assertEquals("Expiration date is invalid", exception.getMessage());
    }

    //UserPaymentオブジェクトのフィールド値を検証するメソッド
    private void assertUserPayment(UserPayment actual, UserPayment expectedUserPayment) {
      assertThat(actual.getId()).isEqualTo(expectedUserPayment.getId());
      assertThat(actual.getUserId()).isEqualTo(expectedUserPayment.getUserId());
      assertThat(actual.getCardNumber()).isEqualTo(expectedUserPayment.getCardNumber());
      assertThat(actual.getCardBrand()).isEqualTo(expectedUserPayment.getCardBrand());
      assertThat(actual.getCardHolder()).isEqualTo(expectedUserPayment.getCardHolder());
      assertThat(actual.getExpirationDate()).isEqualTo(expectedUserPayment.getExpirationDate());
    }
  }
}
