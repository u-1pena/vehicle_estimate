package yuichi.user.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import yuichi.user.management.dto.Request.UserCreateRequest;
import yuichi.user.management.dto.Request.UserDetailCreateRequest;
import yuichi.user.management.dto.Request.UserPaymentCreateRequest;
import yuichi.user.management.dto.UserInformationDto;
import yuichi.user.management.entity.User;
import yuichi.user.management.entity.UserDetail;
import yuichi.user.management.entity.UserPayment;
import yuichi.user.management.helper.CustomizedMockMvc;
import yuichi.user.management.helper.TestHelper;
import yuichi.user.management.service.UserService;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@Import(CustomizedMockMvc.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserService userService;

  TestHelper testHelper;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 指定したユーザーを検索することができる() throws Exception {

      UserInformationDto expectedUserInformation = new UserInformationDto();
      expectedUserInformation.setUser(testHelper.usersMock().get(0));
      expectedUserInformation.setUserDetail(testHelper.userDetailsMock().get(0));
      expectedUserInformation.setUserPayment((List.of(testHelper.userPaymentsMock().get(0))));

      doReturn(List.of(expectedUserInformation)).when(userService).findUserInformationById(1);

      MvcResult result = mockMvc.perform(get("/users/1")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, List.of(expectedUserInformation));
      verify(userService).findUserInformationById(1);
    }

    @Test
    void 指定したユーザーが存在しない場合は404を返す() throws Exception {

      doReturn(Collections.emptyList()).when(userService).findUserInformationById(999);

      mockMvc.perform(get("/users/999")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }

    @Test
    void 指定したアカウント名でユーザー検索ができること() throws Exception {

      UserInformationDto userInformationDto = new UserInformationDto();
      userInformationDto.setUser(testHelper.usersMock().get(0));
      userInformationDto.setUserDetail(testHelper.userDetailsMock().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.userPaymentsMock().get(0)));
      List<UserInformationDto> expected = List.of(userInformationDto);

      doReturn(expected).when(userService).searchUsersByRequestParam("ganmo", null, null, null);

      MvcResult result = mockMvc.perform(get("/users")
              .param("userAccount", "ganmo")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, expected);
      verify(userService).searchUsersByRequestParam("ganmo", null, null, null);
    }

    @Test
    void 指定したアカウント名のユーザーが存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(userService)
          .searchUsersByRequestParam("l", null, null, null);

      mockMvc.perform(get("/users")
              .param("userAccount", "l")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(userService).searchUsersByRequestParam("l", null, null, null);
    }

    @Test
    void 指定した名前でユーザー検索ができること() throws Exception {
      UserInformationDto userInformationDto = new UserInformationDto();
      userInformationDto.setUser(testHelper.usersMock().get(0));
      userInformationDto.setUserDetail(testHelper.userDetailsMock().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.userPaymentsMock().get(0)));
      List<UserInformationDto> expected = List.of(userInformationDto);

      doReturn(expected).when(userService).searchUsersByRequestParam(null, "shima", null, null);

      MvcResult result = mockMvc.perform(get("/users")
              .param("name", "shima")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, expected);
      verify(userService).searchUsersByRequestParam(null, "shima", null, null);
    }

    @Test
    void 指定した名前のユーザーが存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(userService)
          .searchUsersByRequestParam(null, "hoge", null, null);

      mockMvc.perform(get("/users")
              .param("name", "hoge")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(userService).searchUsersByRequestParam(null, "hoge", null, null);
    }

    @Test
    void 指定したカナでユーザー検索ができること() throws Exception {
      UserInformationDto userInformationDto = new UserInformationDto();
      userInformationDto.setUser(testHelper.usersMock().get(0));
      userInformationDto.setUserDetail(testHelper.userDetailsMock().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.userPaymentsMock().get(0)));
      List<UserInformationDto> expected = List.of(userInformationDto);

      doReturn(expected).when(userService).searchUsersByRequestParam(null, null, "ﾕｳｲﾁ", null);

      MvcResult result = mockMvc.perform(get("/users")
              .param("kana", "ﾕｳｲﾁ")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, expected);
      verify(userService).searchUsersByRequestParam(null, null, "ﾕｳｲﾁ", null);
    }

    @Test
    void 指定した読み仮名のユーザーが存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(userService)
          .searchUsersByRequestParam(null, null, "ホゲ", null);

      mockMvc.perform(get("/users")
              .param("kana", "ホゲ")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(userService).searchUsersByRequestParam(null, null, "ホゲ"
          , null);
    }

    @Test
    void 指定したメールアドレスでユーザー検索ができること() throws Exception {
      UserInformationDto userInformationDto = new UserInformationDto();
      userInformationDto.setUser(testHelper.usersMock().get(0));
      userInformationDto.setUserDetail(testHelper.userDetailsMock().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.userPaymentsMock().get(0)));

      List<UserInformationDto> expected = List.of(userInformationDto);

      doReturn(expected).when(userService)
          .searchUsersByRequestParam(null, null, null, "shimaichi5973@gmail.com");

      MvcResult result = mockMvc.perform(get("/users")
              .param("email", "shimaichi5973@gmail.com")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, expected);
      verify(userService).searchUsersByRequestParam(null, null, null, "shimaichi5973@gmail.com");
    }

    @Test
    void 指定したメールアドレスが存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(userService)
          .searchUsersByRequestParam(null, null, null, "hogehoge@test.ne.jp");

      mockMvc.perform(get("/users")
              .param("email", "hogehoge@test.ne.jp")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(userService).searchUsersByRequestParam(null, null, null
          , "hogehoge@test.ne.jp");
    }

    @Test
    void 指定値のない場合は全てのユーザーを取得することができる() throws Exception {
      List<UserInformationDto> expectedList = new ArrayList<>();
      for (int i = 0; i < testHelper.usersMock().size(); i++) {
        UserInformationDto userInformationDto = new UserInformationDto();
        userInformationDto.setUser(testHelper.usersMock().get(i));
        userInformationDto.setUserDetail(testHelper.userDetailsMock().get(i));
        userInformationDto.setUserPayment(testHelper.userPaymentsMock()
            .stream()
            .filter(payment -> payment.getUserId() == userInformationDto.getUser().getId())
            .toList());
        expectedList.add(userInformationDto);
      }

      doReturn(expectedList).when(userService).searchUsersByRequestParam(null, null, null, null);
      String expectedJson = objectMapper.writeValueAsString(expectedList);
      System.out.println(expectedJson);
      MvcResult result = mockMvc.perform(get("/users")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().json(expectedJson))
          .andReturn();
      assertUserResponses(result, expectedList);
      verify(userService).searchUsersByRequestParam(null, null, null, null);
    }


    private void assertUserResponses(MvcResult result, List<UserInformationDto> expectedList)
        throws Exception {
      objectMapper.findAndRegisterModules();
      String responseBody = result.getResponse().getContentAsString();
      List<UserInformationDto> actualList = objectMapper.readValue(responseBody,
          objectMapper.getTypeFactory()
              .constructCollectionType(List.class, UserInformationDto.class));
      assertThat(actualList).isEqualTo(expectedList);
    }
  }

  @Nested
  class CreateClass {

    @Test
    void ユーザー登録時にIDが自動採番され成功メッセージを返すこと() throws Exception {
      // 準備
      User user = new User();
      user.setId(1);
      user.setUserAccount("test");
      user.setEmail("test@example.co.jp");
      // モックの設定
      when(userService.registerUser(any(UserCreateRequest.class))).thenReturn(user);
      String requestBody = """
          {
            "userAccount": "test",
            "email": "test@example.co.jp"
          }
          """;
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("User created"));

      ArgumentCaptor<UserCreateRequest> captor = ArgumentCaptor.forClass(UserCreateRequest.class);
      verify(userService).registerUser(captor.capture());
      UserCreateRequest actual = captor.getValue();
      assertThat(actual.getUserAccount()).isEqualTo("test");
      assertThat(actual.getEmail()).isEqualTo("test@example.co.jp");
    }

    @Test
    void 指定したIDでユーザー詳細情報が登録され成功メッセージを返すこと() throws Exception {
      // 準備
      User user = new User();
      user.setId(1);
      user.setUserAccount("test");
      user.setEmail("test@example.co.jp");

      UserDetail userDetail = new UserDetail();
      userDetail.setId(1);
      userDetail.setFirstName("test");
      userDetail.setLastName("check");
      userDetail.setFirstNameKana("テスト");
      userDetail.setLastNameKana("チェック");
      userDetail.setBirthday(LocalDate.of(1999, 1, 1));
      userDetail.setMobilePhoneNumber("090-1111-1115");
      userDetail.setPassword("Test@12345");

      when(userService.registerUserDetail(eq(1), any(UserDetailCreateRequest.class))).thenReturn(
          userDetail);
      String requestBody = """
          {
              "firstName": "test",
              "lastName": "check",
              "firstNameKana": "テスト",
              "lastNameKana": "チェック",
              "birthday": "1999-01-01",
              "mobilePhoneNumber": "090-1111-1115",
              "password": "Test@12345"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/details/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("UserDetail Created"));

      ArgumentCaptor<UserDetailCreateRequest> captor = ArgumentCaptor.forClass(
          UserDetailCreateRequest.class);
      verify(userService).registerUserDetail(eq(1), captor.capture());
      UserDetailCreateRequest actual = captor.getValue();
      assertThat(actual.getFirstName()).isEqualTo("test");
      assertThat(actual.getLastName()).isEqualTo("check");
      assertThat(actual.getFirstNameKana()).isEqualTo("テスト");
      assertThat(actual.getLastNameKana()).isEqualTo("チェック");
      assertThat(actual.getBirthday()).isEqualTo("1999-01-01");
      assertThat(actual.getMobilePhoneNumber()).isEqualTo("090-1111-1115");
      assertThat(actual.getPassword()).isEqualTo("Test@12345");
    }

    @Test
    void 登録済みのユーザー詳細にIDで紐づけして支払い情報が登録でき成功したメッセージが返されること() throws Exception {
      // 準備
      UserDetail userDetail = new UserDetail();
      UserPayment userPayment = new UserPayment();
      userPayment.setCardNumber("4234567890123456");
      userPayment.setCardHolder("TEST HOGE");
      userPayment.setExpirationDate(YearMonth.of(2028, 1));

      when(userService.registerUserPayment(eq(1), any(UserPaymentCreateRequest.class))).thenReturn(
          userPayment);
      String requestBody = """
                
          {
              "cardNumber": "2719123456785678",
              "cardHolder": "TEST HOGE",
              "expirationDate": "2021-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/payments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("UserPayment Created"));

      ArgumentCaptor<UserPaymentCreateRequest> captor = ArgumentCaptor.forClass(
          UserPaymentCreateRequest.class);
      verify(userService).registerUserPayment(eq(1), captor.capture());
      UserPaymentCreateRequest actual = captor.getValue();
      assertThat(actual.getCardNumber()).isEqualTo("2719123456785678");
      assertThat(actual.getCardHolder()).isEqualTo("TEST HOGE");
      assertThat(actual.getExpirationDate()).isEqualTo("2021-01");
    }

    //Validationのテスト
    @Test
    void ユーザー登録時にアカウント名やEmailが空の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "userAccount": "",
            "email": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "userAccount",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "email",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "userAccount",
                              "message": "アカウントは半角英数字4文字以上32文字以下で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー登録の際にアカウント名が4文字未満の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "userAccount": "tes",
            "email": "test@example.co.jp"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "userAccount",
                              "message": "アカウントは半角英数字4文字以上32文字以下で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー登録の際にアカウント名が32文字を超える場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "userAccount": "123456789012345678901234567890123",
            "email": "test@example.co.jp"
            }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "userAccount",
                              "message": "アカウントは半角英数字4文字以上32文字以下で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー登録の際にメールアドレスの形式が不正な場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "userAccount": "test",
            "email": "testexample.co.jp"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "email",
                              "message": "メールアドレスの形式が不正です"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー詳細を登録の際に名前やカナが空の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "firstName": "",
              "lastName": "",
              "firstNameKana": "",
              "lastNameKana": "",
              "birthday": "",
              "mobilePhoneNumber": "",
              "password": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/details/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "firstNameKana",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "birthday",
                              "message": "birthdayは必須です"
                          },
                          {
                              "field": "mobilePhoneNumber",
                              "message": "mobilePhoneNumberは半角数字でハイフン含め11文字で入力してください"
                          },
                          {
                              "field": "firstName",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "firstName",
                              "message": "firstNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "birthday",
                              "message": "birthdayはyyyy-MM-ddで入力してください"
                          },
                          {
                              "field": "lastNameKana",
                              "message": "lastNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "lastName",
                              "message": "lastNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "lastNameKana",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "lastName",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "password",
                              "message": "passwordは半角英大文字、小文字、数字、記号をそれぞれ1文字以上含む8文字以上16文字以下で入力してください"
                          },
                          {
                              "field": "firstNameKana",
                              "message": "firstNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "password",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "mobilePhoneNumber",
                              "message": "空白は許可されていません"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー詳細を登録の際に名前が正しいフォーマット以外の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "firstName": "てすと",
              "lastName": "てすと",
              "firstNameKana": "てすと",
              "lastNameKana": "てすと",
              "birthday": "1999-01-01",
              "mobilePhoneNumber": "090-1111-1115",
              "password": "てすと"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/details/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "lastName",
                              "message": "lastNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "password",
                              "message": "passwordは半角英大文字、小文字、数字、記号をそれぞれ1文字以上含む8文字以上16文字以下で入力してください"
                          },
                          {
                              "field": "firstNameKana",
                              "message": "firstNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "firstName",
                              "message": "firstNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "lastNameKana",
                              "message": "lastNameKanaは全角カタカナで入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー詳細登録の際に電話番号が正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "firstName": "test",
              "lastName": "check",
              "firstNameKana": "テスト",
              "lastNameKana": "チェック",
              "birthday": "1999-01-01",
              "mobilePhoneNumber": "0901111115",
              "password": "Test@12345"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/details/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "mobilePhoneNumber",
                              "message": "mobilePhoneNumberは半角数字でハイフン含め11文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザー詳細登録の際にパスワードが正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "firstName": "test",
              "lastName": "check",
              "firstNameKana": "テスト",
              "lastNameKana": "チェック",
              "birthday": "1999-01-01",
              "mobilePhoneNumber": "090-1111-1115",
              "password": "test12345"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/details/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "password",
                              "message": "passwordは半角英大文字、小文字、数字、記号をそれぞれ1文字以上含む8文字以上16文字以下で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザーの支払い情報登録の際にカード番号が正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "cardNumber": "123456789012345",
              "cardHolder": "TEST HOGE",
              "expirationDate": "2021-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/payments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "cardNumber",
                              "message": "cardNumberは半角数字で16文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザーの支払い情報登録の際にカード番号が16文字を超える場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "cardNumber": "12345678901234567",
              "cardHolder": "TEST HOGE",
              "expirationDate": "2021-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/payments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "cardNumber",
                              "message": "cardNumberは半角数字で16文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void ユーザーの支払い情報登録の際に全て空の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "cardNumber": "",
              "cardHolder": "",
              "expirationDate": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/payments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "expirationDate",
                              "message": "expirationDateは半角数字でYYYY-MMの形式で入力してください"
                          },
                          {
                              "field": "cardNumber",
                              "message": "cardNumberは半角数字で16文字で入力してください"
                          },
                          {
                              "field": "cardHolder",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "expirationDate",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "cardNumber",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "cardHolder",
                              "message": "cardHolderは半角英大文字とスペースで入力してください"
                          }
                      ]
                  }                  
                  """
          ));
    }

    @Test
    void ユーザーの支払い情報登録の際に有効期限の形式が正しくない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "cardNumber": "1234567890123456",
              "cardHolder": "TEST HOGE",
              "expirationDate": "2028/01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/payments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "BAD_REQUEST",
                      "message": "validation error",
                      "errors": [
                          {
                              "field": "expirationDate",
                              "message": "expirationDateは半角数字でYYYY-MMの形式で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }
  }
}
