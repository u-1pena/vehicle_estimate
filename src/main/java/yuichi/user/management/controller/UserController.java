package yuichi.user.management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.service.UserService;


@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /*ユーザー情報を全て取得する処理
  @RequestParamでaccount,name,kana,emailを受け取り、handleRequestParamでそれぞれの値を受け取り、
  それぞれの値がnullでない場合、それぞれの値をキーにして検索を行う*/
  @GetMapping("/users")
  public List<UserInformationDto> searchUsersByRequestParam(
      @RequestParam(value = "account", required = false) String account,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "kana", required = false) String kana,
      @RequestParam(value = "email", required = false) String email) {
    return userService.searchUsersByRequestParam(account, name, kana, email);
  }

  //ユーザー情報をidで検索する処理
  @GetMapping("/users/{id}")
  public List<UserInformationDto> findUserInformationById(
      @PathVariable("id") int id) {
    return userService.findUserInformationById(id);
  }
}
