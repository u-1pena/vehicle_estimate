package com.u1pena.estimateapi.customer.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressUpdateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerInformationRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerUpdateRequest;
import com.u1pena.estimateapi.customer.dto.request.VehicleCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.VehicleUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.exception.CustomerException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsEmailException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsPhoneNumberException;
import com.u1pena.estimateapi.customer.exception.CustomerException.CustomerNotFoundException;
import com.u1pena.estimateapi.customer.exception.CustomerException.InvalidSearchParameterException;
import com.u1pena.estimateapi.customer.exception.VehicleException;
import com.u1pena.estimateapi.customer.exception.VehicleException.AlreadyExistsVehicleException;
import com.u1pena.estimateapi.customer.helper.CustomizedMockMvc;
import com.u1pena.estimateapi.customer.helper.TestHelper;
import com.u1pena.estimateapi.customer.service.CustomerService;
import java.time.LocalDate;
import java.time.YearMonth;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
@Import(CustomizedMockMvc.class)
class CustomerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  CustomerService customerService;

  TestHelper testHelper;

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 指定した名前で顧客検索ができること() throws Exception {

      CustomerInformationRequest customerInformationRequest = new CustomerInformationRequest();
      customerInformationRequest.setCustomer(testHelper.customerMock().get(0));
      customerInformationRequest.setCustomerAddress(testHelper.customerAddressMock().get(0));
      customerInformationRequest.setVehicles(List.of(testHelper.vehicleMock().get(0)));
      List<CustomerInformationRequest> expected = List.of(customerInformationRequest);

      doReturn(expected).when(customerService)
          .findCustomerInformation("suzuki", null, null);
      mockMvc.perform(MockMvcRequestBuilders.get("/customers")
              .param("name", "suzuki"))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  [
                      {
                          "customer": {
                              "customerId": 1,
                              "lastName": "suzuki",
                              "firstName": "ichiro",
                              "lastNameKana": "スズキ",
                              "firstNameKana": "イチロウ",
                              "email": "ichiro@example.ne.jp",
                              "phoneNumber": "090-1234-5678"
                          },
                          "customerAddress": {
                              "addressId": 1,
                              "customerId": 1,
                              "postalCode": "123-4567",
                              "prefecture": "東京都",
                              "city": "港区",
                              "townAndNumber": "六本木1-1-1",
                              "buildingNameAndRoomNumber": "都心ビル101"
                          },
                          "vehicles": [
                              {
                                  "vehicleId": 1,
                                  "customerId": 1,
                                  "plateRegion": "品川",
                                  "plateCategoryNumber": "123",
                                  "plateHiragana": "あ",
                                  "plateVehicleNumber": "1234",
                                  "make": "toyota",
                                  "model": "DBA-NZE141",
                                  "type": "1NZ",
                                  "year": "2010-12",
                                  "inspectionDate": "2027-12-31",
                                  "active": true
                              }
                          ]
                      }
                  ]
                  """, true
          ));
    }

    @Test
    void 名前で検索をしたとき顧客が存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(customerService)
          .findCustomerInformation("unknown", null, null);

      mockMvc.perform(get("/customers")
              .param("name", "unknown")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(customerService).findCustomerInformation("unknown", null, null);
    }

    @Test
    void 指定した読みかなで顧客検索ができること() throws Exception {

      CustomerInformationRequest customerInformationRequest = new CustomerInformationRequest();
      customerInformationRequest.setCustomer(testHelper.customerMock().get(0));
      customerInformationRequest.setCustomerAddress(testHelper.customerAddressMock().get(0));
      customerInformationRequest.setVehicles(List.of(testHelper.vehicleMock().get(0)));
      List<CustomerInformationRequest> expected = List.of(customerInformationRequest);

      doReturn(expected).when(customerService)
          .findCustomerInformation(null, "スズキ", null);
      mockMvc.perform(MockMvcRequestBuilders.get("/customers")
              .param("kana", "スズキ"))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  [
                      {
                          "customer": {
                              "customerId": 1,
                              "lastName": "suzuki",
                              "firstName": "ichiro",
                              "lastNameKana": "スズキ",
                              "firstNameKana": "イチロウ",
                              "email": "ichiro@example.ne.jp",
                              "phoneNumber": "090-1234-5678"
                          },
                          "customerAddress": {
                              "addressId": 1,
                              "customerId": 1,
                              "postalCode": "123-4567",
                              "prefecture": "東京都",
                              "city": "港区",
                              "townAndNumber": "六本木1-1-1",
                              "buildingNameAndRoomNumber": "都心ビル101"
                          },
                          "vehicles": [
                              {
                                  "vehicleId": 1,
                                  "customerId": 1,
                                  "plateRegion": "品川",
                                  "plateCategoryNumber": "123",
                                  "plateHiragana": "あ",
                                  "plateVehicleNumber": "1234",
                                  "make": "toyota",
                                  "model": "DBA-NZE141",
                                  "type": "1NZ",
                                  "year": "2010-12",
                                  "inspectionDate": "2027-12-31",
                                  "active": true                                  
                              }
                          ]
                      }
                  ]
                  """, true
          ));
    }

    @Test
    void 指定した読みかなの顧客が存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(customerService)
          .findCustomerInformation(null, "ンンン", null);

      mockMvc.perform(get("/customers")
              .param("kana", "ンンン")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(customerService).findCustomerInformation(null, "ンンン", null);
    }

    @Test
    void 指定した車両番号で顧客検索ができること() throws Exception {

      CustomerInformationRequest customerInformationRequest = new CustomerInformationRequest();
      customerInformationRequest.setCustomer(testHelper.customerMock().get(0));
      customerInformationRequest.setCustomerAddress(testHelper.customerAddressMock().get(0));
      customerInformationRequest.setVehicles(List.of(testHelper.vehicleMock().get(0)));
      List<CustomerInformationRequest> expected = List.of(customerInformationRequest);

      doReturn(expected).when(customerService)
          .findInformationByPlateVehicleNumber("1234");
      mockMvc.perform(MockMvcRequestBuilders.get("/vehicles/numbers/{plateVehicleNumber}", "1234"))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  [
                      {
                          "customer": {
                              "customerId": 1,
                              "lastName": "suzuki",
                              "firstName": "ichiro",
                              "lastNameKana": "スズキ",
                              "firstNameKana": "イチロウ",
                              "email": "ichiro@example.ne.jp",
                              "phoneNumber": "090-1234-5678"
                          },
                          "customerAddress": {
                              "addressId": 1,
                              "customerId": 1,
                              "postalCode": "123-4567",
                              "prefecture": "東京都",
                              "city": "港区",
                              "townAndNumber": "六本木1-1-1",
                              "buildingNameAndRoomNumber": "都心ビル101"
                          },
                          "vehicles": [
                              {
                                  "vehicleId": 1,
                                  "customerId": 1,
                                  "plateRegion": "品川",
                                  "plateCategoryNumber": "123",
                                  "plateHiragana": "あ",
                                  "plateVehicleNumber": "1234",
                                  "make": "toyota",
                                  "model": "DBA-NZE141",
                                  "type": "1NZ",
                                  "year": "2010-12",
                                  "inspectionDate": "2027-12-31",
                                  "active": true
                              }
                          ]
                      }
                  ]
                  """, true
          ));
    }

    @Test
    void 指定した車両番号の顧客が存在しない場合は空のリストをかえすこと() throws Exception {
      doReturn(Collections.emptyList()).when(customerService)
          .findInformationByPlateVehicleNumber("0000");
      mockMvc.perform(get("/vehicles/numbers/{plateVehicleNumber}", "0000")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      verify(customerService).findInformationByPlateVehicleNumber("0000");

    }

    @Test
    void 指定したメールアドレスでユーザー検索ができること() throws Exception {
      CustomerInformationRequest customerInformationRequest = new CustomerInformationRequest();
      customerInformationRequest.setCustomer(testHelper.customerMock().get(0));
      customerInformationRequest.setCustomerAddress(testHelper.customerAddressMock().get(0));
      customerInformationRequest.setVehicles(List.of(testHelper.vehicleMock().get(0)));

      List<CustomerInformationRequest> expected = List.of(customerInformationRequest);

      doReturn(expected).when(customerService)
          .findCustomerInformation(null, null, "ichiro@example.ne.jp");
      mockMvc.perform(MockMvcRequestBuilders.get("/customers")
              .param("email", "ichiro@example.ne.jp"))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  [
                      {
                          "customer": {
                              "customerId": 1,
                              "lastName": "suzuki",
                              "firstName": "ichiro",
                              "lastNameKana": "スズキ",
                              "firstNameKana": "イチロウ",
                              "email": "ichiro@example.ne.jp",
                              "phoneNumber": "090-1234-5678"
                          },
                          "customerAddress": {
                              "addressId": 1,
                              "customerId": 1,
                              "postalCode": "123-4567",
                              "prefecture": "東京都",
                              "city": "港区",
                              "townAndNumber": "六本木1-1-1",
                              "buildingNameAndRoomNumber": "都心ビル101"
                          },
                          "vehicles": [
                              {
                                  "vehicleId": 1,
                                  "customerId": 1,
                                  "plateRegion": "品川",
                                  "plateCategoryNumber": "123",
                                  "plateHiragana": "あ",
                                  "plateVehicleNumber": "1234",
                                  "make": "toyota",
                                  "model": "DBA-NZE141",
                                  "type": "1NZ",
                                  "year": "2010-12",
                                  "inspectionDate": "2027-12-31",
                                  "active": true
                              }
                          ]
                      }
                  ]
                  """, true
          ));
    }

    @Test
    void 指定したメールアドレスが存在しない場合は空のリストをかえすこと() throws Exception {

      // モックの設定
      doReturn(Collections.emptyList()).when(customerService)
          .findCustomerInformation(null, null, "hogehoge@test.ne.jp");
      // 実行
      mockMvc.perform(get("/customers")
              .param("email", "hogehoge@test.ne.jp")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()).andExpect(content().string("[]"));
      // 検証
      verify(customerService).findCustomerInformation(null, null, "hogehoge@test.ne.jp");
    }

    @Test
    void 不正なパラメーターで検索をした場合は空のリストを返すこと() throws Exception {
      when(customerService.findCustomerInformation(null, null, null))
          .thenThrow(new InvalidSearchParameterException("Invalid search parameter"));
      mockMvc.perform(MockMvcRequestBuilders.get("/customers")
              .contentType(MediaType.APPLICATION_JSON)
              .param("default", ""))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                       "message": "Invalid search parameter",
                       "error": "Bad Request",
                       "status": "400",
                       "path": "/customers"
                   }
                  """
          ));
    }
  }

  @Nested
  class CreateClass {

    @Test
    void ユーザー登録時にIDが自動採番され成功メッセージを返すこと() throws Exception {

      // 準備
      Customer customer = new Customer();
      customer.setCustomerId(1);
      customer.setLastName("test");
      customer.setFirstName("example");
      customer.setLastNameKana("テスト");
      customer.setFirstNameKana("エグザンプル");
      customer.setEmail("test@example.ne.jp");
      customer.setPhoneNumber("090-1234-5678");

      // モックの設定
      when(customerService.registerCustomer(any(CustomerCreateRequest.class))).thenReturn(customer);
      String requestBody = """
          {
            "lastName": "test",
            "firstName": "example",
            "lastNameKana": "テスト",
            "firstNameKana": "エグザンプル",
            "email": "test@example.ne.jp",
            "phoneNumber": "090-1234-5678"
          }
          """;

      // 実行
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Customer created"));
      // 検証
      ArgumentCaptor<CustomerCreateRequest> captor = ArgumentCaptor.forClass(
          CustomerCreateRequest.class);
      verify(customerService).registerCustomer(captor.capture());
      CustomerCreateRequest actual = captor.getValue();
      assertThat(actual.getLastName()).isEqualTo("test");
      assertThat(actual.getFirstName()).isEqualTo("example");
      assertThat(actual.getLastNameKana()).isEqualTo("テスト");
      assertThat(actual.getFirstNameKana()).isEqualTo("エグザンプル");
      assertThat(actual.getEmail()).isEqualTo("test@example.ne.jp");
      assertThat(actual.getPhoneNumber()).isEqualTo("090-1234-5678");
    }

    @Test
    void 指定したIDで顧客住所が登録され成功メッセージを返すこと() throws Exception {
      // 準備
      Customer customer = new Customer();
      CustomerAddress customerAddress = new CustomerAddress();
      customer.setCustomerId(1);
      customerAddress.setAddressId(1);
      customerAddress.setPostalCode("123-4567");
      customerAddress.setPrefecture(Prefecture.東京都);
      customerAddress.setCity("港区");
      customerAddress.setTownAndNumber("六本木1-1-1");
      customerAddress.setBuildingNameAndRoomNumber("都心ビル101");

      // モックの設定
      when(customerService.registerCustomerAddress(eq(1),
          any(CustomerAddressCreateRequest.class))).thenReturn(
          customerAddress);
      // 実行
      String requestBody = """
          {
              "postalCode": "123-4567",
              "prefecture": "東京都",
              "city": "港区",
              "townAndNumber": "六本木1-1-1",
              "buildingNameAndRoomNumber": "都心ビル101"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers/1/addresses")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("CustomerAddress Created"));

      // 検証
      ArgumentCaptor<CustomerAddressCreateRequest> captor = ArgumentCaptor.forClass(
          CustomerAddressCreateRequest.class);
      verify(customerService).registerCustomerAddress(eq(1), captor.capture());
      CustomerAddressCreateRequest actual = captor.getValue();
      assertThat(actual.getPostalCode()).isEqualTo("123-4567");
      assertThat(actual.getPrefecture()).isEqualTo("東京都");
      assertThat(actual.getCity()).isEqualTo("港区");
      assertThat(actual.getTownAndNumber()).isEqualTo("六本木1-1-1");
      assertThat(actual.getBuildingNameAndRoomNumber()).isEqualTo("都心ビル101");
    }

    @Test
    void 登録済みの顧客にIDで紐づけし車両情報が登録でき成功したメッセージが返されること() throws Exception {
      // 準備
      Customer customer = new Customer();
      Vehicle vehicle = new Vehicle(3, 2, PlateRegion.渋谷, "789", "う", "7890", "honda",
          "DEF456-789012",
          "2EF-GH", YearMonth.of(2021, 6), LocalDate.of(2029, 10, 31), true);
      vehicle.setVehicleId(1);
      customer.setCustomerId(1);
      vehicle.setPlateRegion(PlateRegion.品川);
      vehicle.setPlateCategoryNumber("300");
      vehicle.setPlateHiragana("あ");
      vehicle.setPlateVehicleNumber("1234");
      vehicle.setMake("toyota");
      vehicle.setModel("NZE-141");
      vehicle.setType("1AZ-FE");
      vehicle.setYear(YearMonth.parse("2021-12"));
      vehicle.setInspectionDate(LocalDate.parse("2027-01-01"));
      vehicle.setActive(true);

      when(customerService.registerVehicle(eq(1), any(VehicleCreateRequest.class))).thenReturn(
          vehicle);

      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber": "300",
            "plateHiragana": "あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Vehicle created"));

      ArgumentCaptor<VehicleCreateRequest> captor = ArgumentCaptor.forClass(
          VehicleCreateRequest.class);
      verify(customerService).registerVehicle(eq(1), captor.capture());
      VehicleCreateRequest actual = captor.getValue();
      assertThat(actual.getPlateRegion()).isEqualTo("品川");
      assertThat(actual.getPlateCategoryNumber()).isEqualTo("300");
      assertThat(actual.getPlateHiragana()).isEqualTo("あ");
      assertThat(actual.getPlateVehicleNumber()).isEqualTo("1234");
      assertThat(actual.getMake()).isEqualTo("toyota");
      assertThat(actual.getModel()).isEqualTo("NZE-141");
      assertThat(actual.getType()).isEqualTo("1AZ-FE");
      assertThat(actual.getYear()).isEqualTo("2021-12");
      assertThat(actual.getInspectionDate()).isEqualTo("2027-01-01");
    }

    //Validationのテスト
    @Test
    void 顧客登録時に名前や詳細が空の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "lastName": "",
            "firstName": "",
            "lastNameKana": "",
            "firstNameKana": "",
            "email": "",
            "phoneNumber": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "firstName",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "phoneNumber",
                              "message": "phoneNumberは半角数字でハイフン含め11文字で入力してください"
                          },
                          {
                              "field": "firstName",
                              "message": "firstNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "phoneNumber",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "lastNameKana",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "lastNameKana",
                              "message": "lastNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "lastName",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "lastName",
                              "message": "lastNameは半角英小文字で入力してください"
                          },
                          {
                              "field": "firstNameKana",
                              "message": "firstNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "firstNameKana",
                              "message": "空白は許可されていません"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 顧客を登録する際に名字と名前が正しくないフォーマットの場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "lastName": "4567",
              "firstName": "1234",
              "lastNameKana": "テスト",
              "firstNameKana": "エグザンプル",
              "email": "example@test.co.jp",
              "phoneNumber": "090-1234-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
                              "field": "firstName",
                              "message": "firstNameは半角英小文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 顧客を登録する際に読みかなが正しくないフォーマットの場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "lastName": "test",
              "firstName": "example",
              "lastNameKana": "test",
              "firstNameKana": "example",
              "email": "example@test.co.jp",
              "phoneNumber": "090-1234-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
                              "field": "lastNameKana",
                              "message": "lastNameKanaは全角カタカナで入力してください"
                          },
                          {
                              "field": "firstNameKana",
                              "message": "firstNameKanaは全角カタカナで入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 顧客登録の際にメールアドレスの形式が不正な場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "lastName": "test",
              "firstName": "example",
              "lastNameKana": "テスト",
              "firstNameKana": "エグザンプル",
              "email": "example.co.jp",
              "phoneNumber": "090-1234-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
                              "message": "emailはメールアドレス形式で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 顧客登録の際に電話番号の形式が不正な場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "lastName": "test",
              "firstName": "example",
              "lastNameKana": "テスト",
              "firstNameKana": "エグザンプル",
              "email": "example@test.co.jp",
              "phoneNumber": "09012345678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
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
                              "field": "phoneNumber",
                              "message": "phoneNumberは半角数字でハイフン含め11文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 顧客住所を登録の際に空の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
              "postalCode": "",
              "prefecture": "",
              "city": "",
              "townAndNumber": "",
              "buildingNameAndRoomNumber": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers/1/addresses")
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
                              "field": "townAndNumber",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "prefecture",
                              "message": "都道府県は正しい名前を入力してください"
                          },
                          {
                              "field": "prefecture",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "postalCode",
                              "message": "postalCodeは半角数字でハイフン含め7文字で入力してください"
                          },
                          {
                              "field": "city",
                              "message": "空白は許可されていません"
                          },
                          {
                              "field": "postalCode",
                              "message": "空白は許可されていません"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際に運輸支局が正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "0000",
            "plateHiragana":"あ",
            "plateCategoryNumber":"300",
            "plateVehicleNumber": "1234",
            "make": "honda",
            "model": "JF3-1234567",
            "type": "6BA-JF3",
            "year": "2021-12",
            "inspectionDate": "2025-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "plateRegion",
                              "message": "ナンバープレートの地域名が不正です"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際にプレートひらがなが正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "なにわ",
            "plateHiragana":"ア",
            "plateCategoryNumber":"300",
            "plateVehicleNumber": "1234",
            "make": "honda",
            "model": "JF3-1234567",
            "type": "6BA-JF3",
            "year": "2021-12",
            "inspectionDate": "2025-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "plateHiragana",
                              "message": "plateHiraganaはひらがなで入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際にカテゴリーナンバーが正しいフォーマットでない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "なにわ",
            "plateHiragana":"あ",
            "plateCategoryNumber":"999999",
            "plateVehicleNumber": "1234",
            "make": "honda",
            "model": "JF3-1234567",
            "type": "6BA-JF3",
            "year": "2021-12",
            "inspectionDate": "2025-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "plateCategoryNumber",
                              "message": "plateCategoryNumberは半角数字で3文字で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際にナンバーが空白の場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "なにわ",
            "plateHiragana":"あ",
            "plateCategoryNumber":"300",
            "plateVehicleNumber": "",
            "make": "honda",
            "model": "JF3-1234567",
            "type": "6BA-JF3",
            "year": "2021-12",
            "inspectionDate": "2025-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "plateVehicleNumber",
                              "message": "空白は許可されていません"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際に車両情報のフォーマットが正しくない場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "なにわ",
            "plateHiragana":"あ",
            "plateCategoryNumber":"300",
            "plateVehicleNumber": "1234",
            "make": "ほんだ",
            "model": "ホゲホゲ",
            "type": "ホゲホゲ",
            "year": "2021-12",
            "inspectionDate": "2025-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "make",
                              "message": "メーカーは半角英小文字で入力してください"
                          },
                          {
                              "field": "model",
                              "message": "車体番号は半角英小文字とハイフンで入力してください"
                          },
                          {
                              "field": "type",
                              "message": "型式は半角英小文字とハイフンで入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    @Test
    void 車両登録の際に年式と車検満了が正しくないフォーマットの場合はエラーとなる() throws Exception {
      String requestBody = """
          {
            "plateRegion": "なにわ",
            "plateHiragana":"あ",
            "plateCategoryNumber":"300",
            "plateVehicleNumber": "1234",
            "make": "honda",
            "model": "JF3-1234567",
            "type": "6BA-JF3",
            "year": "平成18年6月",
            "inspectionDate": "R9-12-31"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
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
                              "field": "inspectionDate",
                              "message": "inspectionDateはYYYY-MM-DD形式で入力してください"
                          },
                          {
                              "field": "year",
                              "message": "yearはYYYY-MM形式で入力してください"
                          }
                      ]
                  }
                  """
          ));
    }

    //エラーハンドリングのテスト
    @Test
    void 顧客登録の際にメールアドレスが重複している場合はエラーとなる() throws Exception {
      when(customerService.registerCustomer(any(CustomerCreateRequest.class)))
          .thenThrow(new AlreadyExistsEmailException("Email already exists"));
      String requestBody = """
          {
            "lastName": "test",
            "firstName": "example",
            "lastNameKana": "テスト",
            "firstNameKana": "エグザンプル",
            "email": "ichiro@example.ne.jp",
            "phoneNumber": "090-1234-5678"
           }
           """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "422",
                      "error": "Unprocessable Entity",
                      "message": "Email already exists",
                      "path": "/customers"
                  }
                  """, true
          ));
    }

    @Test
    void 顧客登録の際に電話番号が重複している場合はエラーとなる() throws Exception {
      when(customerService.registerCustomer(any(CustomerCreateRequest.class)))
          .thenThrow(new AlreadyExistsPhoneNumberException("Phone number already exists"));
      String requestBody = """
          {
            "lastName": "test",
            "firstName": "example",
            "lastNameKana": "テスト",
            "firstNameKana": "エグザンプル",
            "email": "ichiro@example.ne.jp",
            "phoneNumber": "090-1234-5678"
           }
           """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "422",
                      "error": "Unprocessable Entity",
                      "message": "Phone number already exists",
                      "path": "/customers"
                  }
                  """, true
          ));
    }

    @Test
    void 顧客登録されていないIDで顧客住所を登録しようとするとエラーとなる() throws Exception {
      when(
          customerService.registerCustomerAddress(eq(999), any(CustomerAddressCreateRequest.class)))
          .thenThrow(new CustomerNotFoundException("Not registered for customer ID:999"));
      String requestBody = """
          {
              "postalCode": "123-4567",
              "prefecture": "東京都",
              "city": "港区",
              "townAndNumber": "六本木1-1-1",
              "buildingNameAndRoomNumber": "都心ビル101"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/customers/999/addresses")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "404",
                      "error": "Not Found",
                      "message": "Not registered for customer ID:999",
                      "path": "/customers/999/addresses"
                  }
                  """, true
          ));
    }

    @Test
    void 顧客登録されていないIDで車両登録しようとするとエラーとなる() throws Exception {
      when(customerService.registerVehicle(eq(999), any(VehicleCreateRequest.class)))
          .thenThrow(new CustomerNotFoundException("Not registered for customer ID:999"));
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/999")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "status": "404",
                      "error": "Not Found",
                      "message": "Not registered for customer ID:999",
                      "path": "/vehicles/999"
                  }
                  """, true
          ));
    }

    @Test
    void すでに車両番号全桁が登録されていたら登録できず重複エラーとなる() throws Exception {
      when(customerService.registerVehicle(eq(1), any(VehicleCreateRequest.class)))
          .thenThrow(new AlreadyExistsVehicleException("Vehicle already exists"));
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/vehicles/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                    {
                        "message": "Vehicle already exists",
                        "error": "Unprocessable Entity",
                        "status": "422",
                        "path": "/vehicles/1"
                    }
                  """, true
          ));
    }
  }

  @Nested
  class DeleteClass {

    @Test
    void 指定したIDの顧客情報を削除できること() throws Exception {
      doNothing().when(customerService).deleteCustomerByCustomerId(1);
      mockMvc.perform(MockMvcRequestBuilders.delete("/customers/{customerId}", 1))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Customer deleted"
                  }
                  """, true
          ));
      verify(customerService).deleteCustomerByCustomerId(1);
    }

    @Test
    void 存在しないIDで顧客情報を削除しようとした場合エラーになること() throws Exception {
      doThrow(
          new CustomerException.CustomerNotFoundException("Not registered for customer ID:999"))
          .when(customerService).deleteCustomerByCustomerId(999);
      mockMvc.perform(MockMvcRequestBuilders.delete("/customers/{customerId}", 999))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Not registered for customer ID:999",
                      "error": "Not Found",
                      "status": "404",
                      "path": "/customers/999"
                  }
                  """, true
          ));
    }

    @Test
    void 指定したIDの車両情報を非アクティブにする() throws Exception {
      doNothing().when(customerService).deleteVehicleByVehicleId(1);
      mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{vehicleId}", 1))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Vehicle deleted"
                  }
                  """, true
          ));
    }

    @Test
    void 指定したIDの車両情報がすでに非アクティブの場合はエラーとなる() throws Exception {
      doThrow(new VehicleException.VehicleInactiveException("Vehicle is already inactive."))
          .when(customerService).deleteVehicleByVehicleId(1);
      mockMvc.perform(MockMvcRequestBuilders.delete("/vehicles/{vehicleId}", 1))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Vehicle is already inactive.",
                      "error": "Bad Request",
                      "status": "400",
                      "path": "/vehicles/1"
                  }
                  """, true
          ));
    }
  }

  @Nested
  class UpdateClass {

    @Test
    void 顧客情報を更新できること() throws Exception {
      String requestBody = """
          {
                      "lastName": "test",
                      "firstName": "example",
                      "lastNameKana": "テスト",
                      "firstNameKana": "エグザンプル",
                      "email": "test@example.ne.jp",
                      "phoneNumber": "080-1111-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Customer updated"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerByCustomerId(eq(1),
          any(CustomerUpdateRequest.class));
    }

    @Test
    void 顧客住所が更新できること() throws Exception {
      String requestBody = """
          {
              "postalCode": "123-4567",
              "prefecture": "東京都",
              "city": "港区",
              "townAndNumber": "六本木1-1-1",
              "buildingNameAndRoomNumber": "都心ビル101"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/addresses/{addressId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "CustomerAddress updated"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerAddressByCustomerId(eq(1),
          any(CustomerAddressUpdateRequest.class));
    }

    @Test
    void 車両情報を更新すること() throws Exception {
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/vehicles/{vehicleId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Vehicle updated"
                  }
                  """, true
          ));
      verify(customerService).updateVehicleByVehicleId(eq(1),
          any(VehicleUpdateRequest.class));
    }

    @Test
    void 存在しない顧客IDで更新するとエラーとなること() throws Exception {
      doThrow(new CustomerException.CustomerNotFoundException("Not registered for customer ID:0"))
          .when(customerService)
          .updateCustomerByCustomerId(eq(0), any(CustomerUpdateRequest.class));
      String requestBody = """
          {
              "lastName": "test",
              "firstName": "example",
              "lastNameKana": "テスト",
              "firstNameKana": "エグザンプル",
              "email": "test@example.ne.jp",
              "phoneNumber": "080-1111-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", 0)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Not registered for customer ID:0",
                      "error": "Not Found",
                      "status": "404",
                      "path": "/customers/0"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerByCustomerId(eq(0), any(CustomerUpdateRequest.class));
    }

    @Test
    void 顧客住所を更新する際IDが存在しない場合エラーとなること() throws Exception {
      doThrow(new CustomerException.CustomerNotFoundException("Not registered for customer ID:0"))
          .when(customerService)
          .updateCustomerAddressByCustomerId(eq(0), any(CustomerAddressUpdateRequest.class));
      String requestBody = """
          {
              "postalCode": "123-4567",
              "prefecture": "東京都",
              "city": "港区",
              "townAndNumber": "六本木1-1-1",
              "buildingNameAndRoomNumber": "都心ビル101"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/addresses/{addressId}", 0)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Not registered for customer ID:0",
                      "error": "Not Found",
                      "status": "404",
                      "path": "/addresses/0"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerAddressByCustomerId(eq(0),
          any(CustomerAddressUpdateRequest.class));
    }

    @Test
    void 車両情報更新の際車両IDが存在しない場合エラーとなること() throws Exception {
      doThrow(new VehicleException.VehicleNotFoundException("Not registered for vehicle ID:0"))
          .when(customerService)
          .updateVehicleByVehicleId(eq(0), any(VehicleUpdateRequest.class));
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/vehicles/{vehicleId}", 0)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Not registered for vehicle ID:0",
                      "error": "Not Found",
                      "status": "404",
                      "path": "/vehicles/0"
                  }
                  """, true
          ));
      verify(customerService).updateVehicleByVehicleId(eq(0), any(VehicleUpdateRequest.class));
    }

    @Test
    void 顧客情報を更新する際電話番号がすでに登録されていた場合エラーとなること() throws Exception {
      doThrow(
          new AlreadyExistsPhoneNumberException("Phone number already exists"))
          .when(customerService)
          .updateCustomerByCustomerId(eq(1), any(CustomerUpdateRequest.class));
      String requestBody = """
          {
                      "lastName": "test",
                      "firstName": "example",
                      "lastNameKana": "テスト",
                      "firstNameKana": "エグザンプル",
                      "email": "test@example.ne.jp",
                      "phoneNumber": "080-1111-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Phone number already exists",
                      "error": "Unprocessable Entity",
                      "status": "422",
                      "path": "/customers/1"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerByCustomerId(eq(1),
          any(CustomerUpdateRequest.class));
    }

    @Test
    void 顧客情報を更新する際Emailがすでに登録済みの場合エラーをかえすこと() throws Exception {
      doThrow(
          new AlreadyExistsEmailException("Email already exists"))
          .when(customerService)
          .updateCustomerByCustomerId(eq(1), any(CustomerUpdateRequest.class));
      String requestBody = """
          {
                      "lastName": "test",
                      "firstName": "example",
                      "lastNameKana": "テスト",
                      "firstNameKana": "エグザンプル",
                      "email": "test@example.ne.jp",
                      "phoneNumber": "080-1111-5678"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Email already exists",
                      "error": "Unprocessable Entity",
                      "status": "422",
                      "path": "/customers/1"
                  }
                  """, true
          ));
      verify(customerService).updateCustomerByCustomerId(eq(1), any(CustomerUpdateRequest.class));
    }

    @Test
    void 車両情報を更新する際車両番号がすでに登録されていた場合エラーとなること() throws Exception {
      doThrow(
          new AlreadyExistsVehicleException("Vehicle already exists"))
          .when(customerService)
          .updateVehicleByVehicleId(eq(1), any(VehicleUpdateRequest.class));
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2021-12",
            "inspectionDate": "2027-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/vehicles/{vehicleId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Vehicle already exists",
                      "error": "Unprocessable Entity",
                      "status": "422",
                      "path": "/vehicles/1"
                  }
                  """, true
          ));
      verify(customerService).updateVehicleByVehicleId(eq(1), any(VehicleUpdateRequest.class));
    }

    @Test
    void 車両情報を更新する初年度が未来の場合エラーをかえすこと() throws Exception {
      doThrow(
          new VehicleException.VehicleYearInvalidException())
          .when(customerService)
          .updateVehicleByVehicleId(eq(1), any(VehicleUpdateRequest.class));
      String requestBody = """
          {
            "plateRegion": "品川",
            "plateCategoryNumber":"300",
            "plateHiragana":"あ",
            "plateVehicleNumber": "1234",
            "make": "toyota",
            "model": "NZE-141",
            "type": "1AZ-FE",
            "year": "2028-12",
            "inspectionDate": "2026-01-01"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.put("/vehicles/{vehicleId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                      "message": "Vehicle year is invalid.",
                      "error": "Bad Request",
                      "status": "400",
                      "path": "/vehicles/1"
                  }
                  """, true
          ));
      verify(customerService).updateVehicleByVehicleId(eq(1), any(VehicleUpdateRequest.class));
    }
  }
}
