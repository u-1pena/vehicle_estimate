package yuichi.user.management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuichi.user.management.converter.UserInformationConverter;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.service.UserService;
//@Valid


//ユーザーEntityについて
/*
ユーザー情報:User
ユーザー詳細:UserDetail
ユーザー支払い情報:UserPayment
 */
@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  //ユーザー情報を全て取得する処理
  @GetMapping("/users/info")
  public List<UserInformationConverter> findAllUsersInfo() {
    return userService.findAllUser();
  }

  //ユーザー情報をidで検索する処理
  @GetMapping("/user/info/{id}")
  public List<UserInformationConverter> searchUserInfoById(
      @PathVariable("id") int id) {
    return userService.searchUsersInfoById(id);
  }

  //ユーザーを名前で検索する処理

  @GetMapping("users/names")
  public List<User> searchUserByName(@RequestParam("search") String account) {
    return userService.searchByAccountName(account);
  }

  //アカウント名でユーザー情報を検索する処理
  @GetMapping("users/info/names")
  public List<UserInformationConverter> searchUserInfoByAccount(
      @RequestParam("search") String account) {
    return userService.searchInfoByAccountName(account);
  }

  //部分一致：ユーザー詳細を読み仮名で検索する処理
  @GetMapping("/users/details/names/kana")
  public List<UserDetail> searchByFullNameKana(@RequestParam String search) {
    List<UserDetail> userDetail = userService.searchByFullNameKana(search);
    return userDetail;
  }

  //部分一致：ユーザー詳細を名前で検索する処理
  @GetMapping("/users/details/names")
  public List<UserDetail> searchByUserName(@RequestParam String search) {
    List<UserDetail> userDetail = userService.searchByDetailName(search);
    return userDetail;
  }

  @GetMapping("/users")
  public List<User> findUser() {
    return userService.findUser();
  }

  @GetMapping("/users/{id}")
  public User searchUserById(@PathVariable("id") int id) {
    return userService.findUserById(id);
  }

  @GetMapping("/users/details")
  public List<UserDetail> findUserDetail() {
    return userService.findUserDetail();
  }

  @GetMapping("/users/details/{id}")
  public UserDetail searchUserDetailById(@PathVariable("id") int id) {
    return userService.findUserDetailById(id);
  }

  @GetMapping("/users/payments/{userId}")
  public List<UserPayment> searchUsersPaymentsAllByUserId(
      @PathVariable("userId") int userId) {
    return userService.findAllPaymentsByUserId(userId);
  }

  @GetMapping("/users/email")
  public List<User> SearchByEmail(@RequestParam String search) {
    List<User> user = userService.searchByEmail(search);
    return user;
  }

  @GetMapping("/users/info/email")
  public List<UserInformationConverter> searchUserInfoByEmail(
      @RequestParam String search) {
    return userService.searchUserInfoByEmail(search);
  }
}
