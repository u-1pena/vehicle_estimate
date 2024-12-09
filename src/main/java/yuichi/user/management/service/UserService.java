package yuichi.user.management.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import yuichi.user.management.controller.exception.UserNotFoundException;
import yuichi.user.management.converter.UserInformationConverter;
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


  public List<UserInformationConverter> handleRequestParam(String account, String name,
      String kana, String email) {
    if (account != null && !account.isEmpty()) {
      return searchInfoByAccountName(account);
    }
    if (name != null && !name.isEmpty()) {
      return searchUserInfoByName(name);
    }
    if (kana != null && !kana.isEmpty()) {
      return searchUserInfoByFullNameKana(kana);
    }
    if (email != null && !email.isEmpty()) {
      return searchUserInfoByEmail(email);
    }
    return findAllUsers();
  }

  public List<UserInformationConverter> findAllUsers() {
    List<User> users = findUser();
    List<UserDetail> userDetails = findUserDetail();
    List<UserPayment> userPayments = findUserPayment();

    Map<Integer, UserDetail> detailMap = userDetails.stream()
        .collect(Collectors.toMap(UserDetail::getId, detail -> detail));

    Map<Integer, List<UserPayment>> paymentMap = userPayments.stream()
        .collect(Collectors.groupingBy(UserPayment::getUserId));

    return users.stream()
        .map(user -> {
          UserInformationConverter converter = new UserInformationConverter();
          converter.setUser(user);
          converter.setUserDetail(detailMap.get(user.getId()));
          converter.setUserPayment(paymentMap.get(user.getId()));
          return converter;
        })
        .collect(Collectors.toList());

  }

  public List<User> findUser() {
    return userRepository.findUser();
  }

  public List<UserDetail> findUserDetail() {
    return userRepository.findUserDetail();
  }

  public List<UserPayment> findUserPayment() {
    return userRepository.findUserPayment();
  }

  public User findUserById(int id) {
    return userRepository.findUserById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  //id検索

  public UserDetail findUserDetailById(int id) {
    return userRepository.findUserDetailById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  public List<UserPayment> searchUserPaymentById(int userId) {
    List<UserPayment> userPayments = userRepository.findAllPaymentsByUserId(userId);
    return userPayments;
  }

  public List<UserInformationConverter> searchUsersInfoById(int id) {
    User user = findUserById(id);
    UserDetail userDetail = findUserDetailById(id);
    List<UserPayment> userPayments = searchUserPaymentById(id);

    UserInformationConverter converter = new UserInformationConverter();
    converter.setUser(user);
    converter.setUserDetail(userDetail);
    converter.setUserPayment(userPayments);

    return List.of(converter);
  }

  public List<UserInformationConverter> searchInfoByAccountName(String account) {
    List<User> users = searchByAccountName(account);
    return users.stream()
        .map(this::toSearchUserInformationConverterForUser)
        .collect(Collectors.toList());
  }


  //ユーザー情報を取得する処理 完全一致のEmailで検索しユーザー情報を照会します
  public List<UserInformationConverter> searchUserInfoByEmail(String email) {
    List<User> users = searchByEmail(email);
    return users.stream()
        .map(this::toSearchUserInformationConverterForUser)
        .collect(Collectors.toList());
  }

  private UserInformationConverter toSearchUserInformationConverterForUser(User user) {

    UserInformationConverter converter = new UserInformationConverter();
    converter.setUser(user);
    converter.setUserDetail(findUserDetailById(user.getId()));
    converter.setUserPayment(searchUserPaymentById(user.getId()));
    return converter;
  }

  public List<User> searchByAccountName(String account) {
    List<User> user = userRepository.searchByAccountName(account);
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + account);
    }
    return user;
  }

  public List<User> searchByEmail(String email) {
    List<User> user = userRepository.searchByEmail(email);
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with email: " + email);
    }
    return user;
  }

  public List<UserInformationConverter> searchUserInfoByName(String name) {
    List<UserDetail> userDetails = searchByDetailName(name);
    return userDetails.stream()
        .map(this::toSearchUserInformationConverterForUserDetail)
        .collect(Collectors.toList());
  }

  public List<UserInformationConverter> searchUserInfoByFullNameKana(String kana) {
    List<UserDetail> userDetails = searchByFullNameKana(kana);
    return userDetails.stream()
        .map(this::toSearchUserInformationConverterForUserDetail)
        .collect(Collectors.toList());
  }


  private UserInformationConverter toSearchUserInformationConverterForUserDetail(
      UserDetail userDetail) {
    UserInformationConverter converter = new UserInformationConverter();
    converter.setUser(findUserById(userDetail.getId()));
    converter.setUserDetail(userDetail);
    converter.setUserPayment(searchUserPaymentById(userDetail.getId()));
    return converter;
  }

  public List<UserDetail> searchByDetailName(String name) {
    List<UserDetail> UserDetail = userRepository.searchByDetailName(name);
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + name);
    }
    return UserDetail;
  }

  public List<UserDetail> searchByFullNameKana(String kana) {
    List<UserDetail> UserDetail = userRepository.searchByFullNameKana(kana);
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + kana);
    }
    return UserDetail;
  }

}
