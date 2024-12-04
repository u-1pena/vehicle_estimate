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

  /*
  ユーザー情報を全て取得する処理
  findUser,findUserDetail, findUserPaymentで、各情報を取得
  ユーザーはそのまま順に取得し、ユーザー詳細はidをキーにして取得し、ユーザー支払いはuserIdをキーにして取得
  ユーザー詳細はtoMapでidをキーにしてMapに変換し、ユーザー支払いはgroupingByでuserIdをキーにしてMapに変換
  */
  public List<UserInformationConverter> findAllUser() {
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

  public UserDetail findUserDetailById(int id) {
    return userRepository.findUserDetailById(id)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  public List<UserPayment> findAllPaymentsByUserId(int userId) {
    List<UserPayment> userPayments = userRepository.findAllPaymentsByUserId(userId);
    if (userPayments.isEmpty()) {
      throw new UserNotFoundException("user not found with id: " + userId);
    }
    return userPayments;
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
        .map(this::toSearchUserInformationConverter)
        .collect(Collectors.toList());
  }

  //ユーザー情報を取得する処理 完全一致のEmailで検索しユーザー情報を照会します
  public List<UserInformationConverter> searchUserInfoByEmail(String email) {
    List<User> users = searchByEmail(email);
    return users.stream()
        .map(this::toSearchUserInformationConverter)
        .collect(Collectors.toList());
  }

  //ユーザー情報を取得する処理
  private UserInformationConverter toSearchUserInformationConverter(User user) {

    UserInformationConverter converter = new UserInformationConverter();
    converter.setUser(user);
    converter.setUserDetail(findUserDetailById(user.getId()));
    converter.setUserPayment(searchUserPaymentById(user.getId()));
    return converter;
  }

  //部分一致：アカウント名検索
  public List<User> searchByAccountName(String account) {
    List<User> user = userRepository.searchByAccountName(account);
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + account);
    }
    return user;
  }

  //部分一致：名字・名前をカナ検索
  public List<UserDetail> searchByFullNameKana(String kana) {
    List<UserDetail> UserDetail = userRepository.searchByFullNameKana(kana);
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + kana);
    }
    return UserDetail;
  }

  //部分一致：名字・名前を検索
  public List<UserDetail> searchByDetailName(String name) {
    List<UserDetail> UserDetail = userRepository.searchByDetailName(name);
    if (UserDetail.isEmpty()) {
      throw new UserNotFoundException("user not found with name: " + name);
    }
    return UserDetail;
  }

  //完全一致のみ
  public List<User> searchByEmail(String email) {
    List<User> user = userRepository.searchByEmail(email);
    if (user.isEmpty()) {
      throw new UserNotFoundException("user not found with email: " + email);
    }
    return user;
  }


}
