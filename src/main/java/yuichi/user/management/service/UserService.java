package yuichi.user.management.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import yuichi.user.management.controller.exception.UserDetailAlreadyExistsException;
import yuichi.user.management.controller.exception.UserNotFoundException;
import yuichi.user.management.converter.UserCreateConverter;
import yuichi.user.management.converter.UserDetailCreateConverter;
import yuichi.user.management.converter.UserInformationConverter;
import yuichi.user.management.converter.UserPaymentCreateConverter;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.mapper.UserRepository;


@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*クエリパラメーターがなければ全件取得。@RequestParamがaccount,name,kana,emailの場合
   * 条件検索へ移行するメソッド*/
  public List<UserInformationDto> searchUsersByRequestParam(String account, String name,
      String kana,
      String email) {
    String pramName = defineSearchCriteria(account, name, kana,
        email);//英語弱者のメモ：defineSearchCriteria(検索条件を定義）

    //switch文でパラメーターの可読性アップ！確かに見やすいけど、if文のほうがコードが短くなるので、どちらがいいかは微妙かも・・
    return switch (pramName) {
      case "account" -> findInformationByAccountName(account);
      case "name" -> findUserInformationByName(name);
      case "kana" -> findUserInformationByFullNameKana(kana);
      case "email" -> findUserInformationByEmail(email);
      default -> findAll();
    };
  }

  /*検索条件を定義するメソッド
   * 例：accountがnullでもなく空白でもなければ"accountを返す検索条件を判定するメソッド"*/
  private String defineSearchCriteria(String account, String name, String kana, String email) {
    if (account != null && !account.isBlank()) {
      return "account";
    }
    if (name != null && !name.isBlank()) {
      return "name";
    }
    if (kana != null && !kana.isBlank()) {
      return "kana";
    }
    if (email != null && !email.isBlank()) {
      return "email";
    }
    return "default";
  }

  //findAllUsersをfindAllに変更。findAllUserInformationも考えたが、シンプルに全部を取得するということでfindAllに変更
  public List<UserInformationDto> findAll() {
    List<User> users = findAllUsers();
    List<UserDetail> userDetails = findAllUserDetails();
    List<UserPayment> userPayments = findAllUserPayments();

    Map<Integer, UserDetail> detailMap = userDetails.stream()
        .collect(Collectors.toMap(UserDetail::getId, detail -> detail));

    Map<Integer, List<UserPayment>> paymentMap = userPayments.stream()
        .collect(Collectors.groupingBy(UserPayment::getUserId));

    return users.stream()
        .map(user -> {
          UserDetail detail = detailMap.get(user.getId());
          List<UserPayment> payments = paymentMap.getOrDefault(user.getId(),
              Collections.emptyList());
          return UserInformationConverter.convertToUserInformationDto(user, detail, payments);
        })
        .collect(Collectors.toList());
  }


  private List<User> findAllUsers() {
    return userRepository.findAllUsers();
  }

  private List<UserDetail> findAllUserDetails() {
    return userRepository.findAllUserDetails();
  }

  private List<UserPayment> findAllUserPayments() {
    return userRepository.findAllUserPayments();
  }

  public User findUserById(int id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  public UserDetail findUserDetailById(int id) {
    return userRepository.findUserDetailById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  private List<UserPayment> findUserPaymentsById(int userId) {
    return userRepository.findUserPaymentsByUserId(userId);
  }

  public List<UserInformationDto> findUserInformationById(int id) {
    User user = findUserById(id);
    UserDetail userDetail = findUserDetailById(id);
    List<UserPayment> userPayments = findUserPaymentsById(id);
    UserInformationDto userInformationDto = UserInformationConverter.convertToUserInformationDto(
        user, userDetail, userPayments);
    return List.of(userInformationDto);
  }

  private List<UserInformationDto> findUserByCriteria(List<User> users) {
    return users.stream()
        .map(this::convertToUserInformationDtoFromUser)
        .collect(Collectors.toList());
  }

  private List<UserInformationDto> findInformationByAccountName(String account) {
    List<User> users = findByAccountName(account);
    return findUserByCriteria(users);

  }


  //ユーザー情報を取得する処理 完全一致のEmailで検索しユーザー情報を照会します
  private List<UserInformationDto> findUserInformationByEmail(String email) {
    List<User> users = findByEmail(email);
    return findUserByCriteria(users);
  }

  private UserInformationDto convertToUserInformationDtoFromUser(User user) {
    UserDetail userDetail = findUserDetailById(user.getId());
    List<UserPayment> userPayments = findUserPaymentsById(user.getId());
    return UserInformationConverter.convertToUserInformationDto(user, userDetail, userPayments);

  }

  private List<User> findByAccountName(String account) {
    List<User> user = userRepository.findByAccountName(account);
    return user;
  }

  private List<User> findByEmail(String email) {
    List<User> user = userRepository.findByEmail(email);
    return user;
  }

  private List<UserInformationDto> findUserDetailByCriteria(List<UserDetail> userDetails) {
    return userDetails.stream()
        .map(this::convertToUserInformationDtoFromUserDetail)
        .collect(Collectors.toList());
  }

  private List<UserInformationDto> findUserInformationByName(String name) {
    List<UserDetail> userDetails = findByDetailName(name);
    return findUserDetailByCriteria(userDetails);
  }

  private List<UserInformationDto> findUserInformationByFullNameKana(String kana) {
    List<UserDetail> userDetails = findByFullNameKana(kana);
    return findUserDetailByCriteria(userDetails);
  }


  private UserInformationDto convertToUserInformationDtoFromUserDetail(UserDetail userDetail) {
    User user = findUserById(userDetail.getId());
    List<UserPayment> userPayments = findUserPaymentsById(userDetail.getId());
    return UserInformationConverter.convertToUserInformationDto(user, userDetail, userPayments);
  }


  private List<UserDetail> findByDetailName(String name) {
    List<UserDetail> UserDetail = userRepository.findByDetailName(name);
    return UserDetail;
  }

  private List<UserDetail> findByFullNameKana(String kana) {
    List<UserDetail> UserDetail = userRepository.findByFullNameKana(kana);
    return UserDetail;
  }

  public User registerUser(UserCreateRequest userCreateRequest) {
    checkAlreadyExistEmail(userCreateRequest.getEmail());
    User user = UserCreateConverter.userConvertToEntity(userCreateRequest);
    createUser(user);
    return user;
  }

  private void checkAlreadyExistEmail(String email) {
    userRepository.checkAlreadyExistByEmail(email)
        .ifPresent(userDetail -> {
          throw new UserDetailAlreadyExistsException(
              "UserDetail already exists with email: " + email);
        });
  }

  private void createUser(User user) {
    userRepository.insertUser(user);
  }

  public UserDetail registerUserDetail(int id, UserDetailCreateRequest userDetailCreateRequest) {
    User user = findUserById(id);
    checkAlreadyExistUserDetail(id);
    checkAlreadyExistMobilePhoneNumber(userDetailCreateRequest.getMobilePhoneNumber());
    UserDetail userDetail = UserDetailCreateConverter.userDetailConvertToEntity(user,
        userDetailCreateRequest);
    createUserDetail(userDetail);
    return userDetail;
  }

  private void checkAlreadyExistUserDetail(int id) {
    userRepository.findUserDetailById(id)
        .map(userDetail -> {
          throw new UserDetailAlreadyExistsException("userDetail already exist with id: " + id);
        });
  }

  private void checkAlreadyExistMobilePhoneNumber(String mobilePhoneNumber) {
    userRepository.CheckAlreadyExistByMobilePhoneNumber(mobilePhoneNumber)
        .ifPresent(userDetail -> {
          throw new UserDetailAlreadyExistsException(
              "UserDetail already exists with mobilePhoneNumber: " + mobilePhoneNumber
          );
        });
  }

  private void createUserDetail(UserDetail userDetail) {
    userRepository.insertUserDetail(userDetail);
  }

  public UserPayment registerUserPayment(int id,
      UserPaymentCreateRequest userPaymentCreateRequest) {
    UserDetail userDetail = findUserDetailById(id);
    checkAlreadyExistCardNumber(userPaymentCreateRequest.getCardNumber());
    UserPayment userPayment = UserPaymentCreateConverter.userPaymentConvertToEntity(userDetail,
        userPaymentCreateRequest, this);
    createUserPayment(userPayment);
    return userPayment;
  }

  private void checkAlreadyExistCardNumber(String cardNumber) {
    userRepository.checkAlreadyExistByCardNumber(cardNumber)
        .ifPresent(userPayment -> {
          throw new UserDetailAlreadyExistsException(
              "UserPayment already exists with cardNumber: " + cardNumber
          );
        });
  }

  public String identifyCardBrand(String cardNumber) {
    return switch (cardNumber.substring(0, 1)) {
      case "2" -> "American Express";
      case "3" -> "JCB";
      case "4" -> "VISA";
      case "5" -> "MasterCard";
      case "6" -> "Discover";
      default -> throw new IllegalArgumentException("Invalid card number");
    };
  }

  private void createUserPayment(UserPayment userPayment) {
    userRepository.insertUserPayment(userPayment);
  }
}
