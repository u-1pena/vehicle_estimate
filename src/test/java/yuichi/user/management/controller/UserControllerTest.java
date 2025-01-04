package yuichi.user.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import yuichi.user.management.dto.UserInformationDto;
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

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 指定したユーザーを検索することができる() throws Exception {

      UserInformationDto expectedUserInformation = new UserInformationDto();
      expectedUserInformation.setUser(testHelper.mockUsers().get(0));
      expectedUserInformation.setUserDetail(testHelper.mockUserDetails().get(0));
      expectedUserInformation.setUserPayment((List.of(testHelper.mockUserPayments().get(0))));

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
      userInformationDto.setUser(testHelper.mockUsers().get(0));
      userInformationDto.setUserDetail(testHelper.mockUserDetails().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.mockUserPayments().get(0)));
      List<UserInformationDto> expected = List.of(userInformationDto);

      doReturn(expected).when(userService).searchUsersByRequestParam("ganmo", null, null, null);

      MvcResult result = mockMvc.perform(get("/users")
              .param("account", "ganmo")
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
              .param("account", "l")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(userService).searchUsersByRequestParam("l", null, null, null);
    }

    @Test
    void 指定した名前でユーザー検索ができること() throws Exception {
      UserInformationDto userInformationDto = new UserInformationDto();
      userInformationDto.setUser(testHelper.mockUsers().get(0));
      userInformationDto.setUserDetail(testHelper.mockUserDetails().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.mockUserPayments().get(0)));
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
      userInformationDto.setUser(testHelper.mockUsers().get(0));
      userInformationDto.setUserDetail(testHelper.mockUserDetails().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.mockUserPayments().get(0)));
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
      userInformationDto.setUser(testHelper.mockUsers().get(0));
      userInformationDto.setUserDetail(testHelper.mockUserDetails().get(0));
      userInformationDto.setUserPayment(List.of(testHelper.mockUserPayments().get(0)));

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
      for (int i = 0; i < testHelper.mockUsers().size(); i++) {
        UserInformationDto userInformationDto = new UserInformationDto();
        userInformationDto.setUser(testHelper.mockUsers().get(i));
        userInformationDto.setUserDetail(testHelper.mockUserDetails().get(i));
        userInformationDto.setUserPayment(testHelper.mockUserPayments()
            .stream()
            .filter(payment -> payment.getUserId() == userInformationDto.getUser().getId())
            .toList());
        expectedList.add(userInformationDto);
      }

      doReturn(expectedList).when(userService).searchUsersByRequestParam(null, null, null, null);

      MvcResult result = mockMvc.perform(get("/users")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andReturn();
      assertUserResponses(result, expectedList);
      verify(userService).searchUsersByRequestParam(null, null, null, null);
    }

    private void assertUserResponses(MvcResult result,
        List<UserInformationDto> expectedList)
        throws Exception {
      String responseBody = result.getResponse().getContentAsString();
      System.out.println("Response Body: " + result.getResponse().getContentAsString());

      // 空リストの場合の対応
      if (expectedList.isEmpty()) {
        assertThat(responseBody).isEqualTo("[]"); // 空のJSONリストであることを検証
        return;
      }

      for (UserInformationDto expected : expectedList) {
        assertThat(responseBody).contains("\"id\":" + expected.getUser().getId());
        assertThat(responseBody).contains(
            "\"account\":\"" + expected.getUser().getAccount() + "\"");
        assertThat(responseBody).contains("\"email\":\"" + expected.getUser().getEmail() + "\"");
        assertThat(responseBody).contains(
            "\"firstName\":\"" + expected.getUserDetail().getFirstName() + "\"");
        assertThat(responseBody).contains(
            "\"lastName\":\"" + expected.getUserDetail().getLastName() + "\"");
        assertThat(responseBody).contains(
            "\"firstNameKana\":\"" + expected.getUserDetail().getFirstNameKana() + "\"");
        assertThat(responseBody).contains(
            "\"lastNameKana\":\"" + expected.getUserDetail().getLastNameKana() + "\"");
        assertThat(responseBody).contains(
            "\"birthday\":\"" + expected.getUserDetail().getBirthday() + "\"");
        assertThat(responseBody).contains(
            "\"mobilePhoneNumber\":\"" + expected.getUserDetail().getMobilePhoneNumber() + "\"");
        assertThat(responseBody).contains(
            "\"password\":\"" + expected.getUserDetail().getPassword() + "\"");

        for (int j = 0; j < expected.getUserPayment().size(); j++) {
          assertThat(responseBody).contains("\"id\":" + expected.getUserPayment().get(j).getId());
          assertThat(responseBody).contains(
              "\"userId\":" + expected.getUserPayment().get(j).getUserId());
          assertThat(responseBody).contains(
              "\"cardNumber\":\"" + expected.getUserPayment().get(j).getCardNumber() + "\"");
          assertThat(responseBody).contains(
              "\"cardBrand\":\"" + expected.getUserPayment().get(j).getCardBrand() + "\"");
          assertThat(responseBody).contains(
              "\"cardHolder\":\"" + expected.getUserPayment().get(j).getCardHolder() + "\"");
          assertThat(responseBody).contains(
              "\"expirationDate\":\"" + expected.getUserPayment().get(j).getExpirationDate()
                  + "\"");
        }
      }
    }
  }
}
