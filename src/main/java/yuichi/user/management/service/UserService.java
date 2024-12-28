package yuichi.user.management.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import yuichi.user.management.controller.exception.UserNotFoundException;
import yuichi.user.management.converter.UserInformationConverter;
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
    List<UserPayment> userPaymentsList = userRepository.findAllUserPayments();
    return userPaymentsList != null ? userPaymentsList : Collections.emptyList();
  }

  private User findUserById(int id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  private UserDetail findUserDetailById(int id) {
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
    if (users.isEmpty()) {
      throw new UserNotFoundException("user not found with account: " + account);
    }
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
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with account: " + account);
    }
    return user;
  }

  private List<User> findByEmail(String email) {
    List<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with email: " + email);
    }
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
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + name);
    }
    return UserDetail;
  }

  private List<UserDetail> findByFullNameKana(String kana) {
    List<UserDetail> UserDetail = userRepository.findByFullNameKana(kana);
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + kana);
    }
    return UserDetail;
  }

}
