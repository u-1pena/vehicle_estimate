package yuichi.user.management.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import yuichi.user.management.controller.Response.CreateResponse;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
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
      @RequestParam(value = "userAccount", required = false) String account,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "kana", required = false) String kana,
      @RequestParam(value = "email", required = false) String email) {
    List<UserInformationDto> result = userService.searchUsersByRequestParam(account, name, kana,
        email);
    return result;
  }

  //ユーザー情報をidで検索する処理
  @GetMapping("/users/{id}")
  public ResponseEntity<List<UserInformationDto>> findUserInformationById(
      @PathVariable("id") int id) {
    List<UserInformationDto> users = userService.findUserInformationById(id);
    if (users.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(users);
  }

  @PostMapping("/users")
  public ResponseEntity<CreateResponse> insertUser(
      @RequestBody @Valid UserCreateRequest userCreateRequest, UriComponentsBuilder uriBuilder) {
    User user = userService.registerUser(userCreateRequest);
    URI location = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
    CreateResponse body = new CreateResponse("User created");
    return ResponseEntity.created(location).body(body);
  }

  @PostMapping("user-details/{id}")
  public ResponseEntity<CreateResponse> createUserDetail(
      @PathVariable("id") int id,
      @RequestBody @Valid UserDetailCreateRequest userDetailCreateRequest, // 専用リクエストクラス
      UriComponentsBuilder uriBuilder) {
    UserDetail userDetail = userService.registerUserDetail(id, userDetailCreateRequest);
    URI location = uriBuilder.path("/details/{id}").buildAndExpand(userDetail.getId()).toUri();
    CreateResponse body = new CreateResponse("UserDetail Created");
    return ResponseEntity.created(location).body(body);
  }

  @PostMapping("/payments/{id}")
  public ResponseEntity<CreateResponse> createUserPayment(
      @PathVariable("id") int id,
      @RequestBody @Valid UserPaymentCreateRequest userPaymentCreateRequest,
      UriComponentsBuilder uriBuilder) {
    UserPayment userPayment = userService.registerUserPayment(id, userPaymentCreateRequest);
    URI location = uriBuilder.path("/payments/{id}").buildAndExpand(userPayment.getId()).toUri();
    CreateResponse body = new CreateResponse("UserPayment Created");
    return ResponseEntity.created(location).body(body);
  }
}
