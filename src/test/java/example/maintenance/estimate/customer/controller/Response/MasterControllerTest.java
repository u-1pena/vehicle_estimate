package example.maintenance.estimate.customer.controller.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import example.maintenance.estimate.customer.controller.exception.MasterException;
import example.maintenance.estimate.customer.controller.exception.MasterException.MaintenanceGuideAlreadyExistsException;
import example.maintenance.estimate.customer.dto.request.master.MaintenanceGuideCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCategoryCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCreateRequest;
import example.maintenance.estimate.customer.entity.master.MaintenanceGuide;
import example.maintenance.estimate.customer.entity.master.Product;
import example.maintenance.estimate.customer.entity.master.ProductCategory;
import example.maintenance.estimate.customer.helper.CustomizedMockMvc;
import example.maintenance.estimate.customer.helper.MasterTestHelper;
import example.maintenance.estimate.customer.service.MaintenanceGuideService;
import java.math.BigDecimal;
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

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MasterController.class)
@Import(CustomizedMockMvc.class)
class MasterControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MaintenanceGuideService maintenanceGuideService;

  MasterTestHelper masterTestHelper;


  @BeforeEach
  void setUp() {
    masterTestHelper = new MasterTestHelper();
  }

  @Nested
  class CreateClass {

    @Test
    void メンテナンスガイド作成時に自動採番され成功メッセージが返ってくること() throws Exception {
      MaintenanceGuide maintenanceGuide = masterTestHelper.maintenanceGuideMock().get(0);

      // Arrange
      when(maintenanceGuideService.registerMaintenanceGuide(
          any(MaintenanceGuideCreateRequest.class)))
          .thenReturn(maintenanceGuide);
      String requestBody = """
          {
            "make": "toyota",
            "vehicleName": "アクア",
            "model": "TEST1234",
            "type": "EG-1234",
            "startYear": "2000-10",
            "endYear": "2020-01",
            "oilViscosity": "0w-20",
            "oilQuantityWithFilter": 3.5,
            "oilQuantityWithoutFilter": 3.0,
            "oilFilterPartNumber": "12345",
            "carWashSize": "S"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/maintenance-guides")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Maintenance guide created successfully"));

      ArgumentCaptor<MaintenanceGuideCreateRequest> captor = ArgumentCaptor.forClass(
          MaintenanceGuideCreateRequest.class);
      verify(maintenanceGuideService).registerMaintenanceGuide(captor.capture());
      MaintenanceGuideCreateRequest actual = captor.getValue();
      assertThat(actual.getMake()).isEqualTo("toyota");
      assertThat(actual.getVehicleName()).isEqualTo("アクア");
      assertThat(actual.getModel()).isEqualTo("TEST1234");
      assertThat(actual.getType()).isEqualTo("EG-1234");
      assertThat(actual.getStartYear()).isEqualTo("2000-10");
      assertThat(actual.getEndYear()).isEqualTo("2020-01");
      assertThat(actual.getOilViscosity()).isEqualTo("0w-20");
      assertThat(actual.getOilQuantityWithFilter()).isEqualTo(3.5);
      assertThat(actual.getOilQuantityWithoutFilter()).isEqualTo(3.0);
      assertThat(actual.getOilFilterPartNumber()).isEqualTo("12345");
      assertThat(actual.getCarWashSize()).isEqualTo("S");
    }


    @Test
    void 商品カテゴリー作成時にIDは自動採番され成功メッセージがかえってくること() throws Exception {
      ProductCategory productCategory = new ProductCategory();
      productCategory.setCategoryName("test");

      // Arrange
      when(maintenanceGuideService.registerProductCategory(any(ProductCategoryCreateRequest.class)))
          .thenReturn(productCategory);
      String requestBody = """
          {
            "categoryName": "test"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/product-categories")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Product category created successfully"));

      ArgumentCaptor<ProductCategoryCreateRequest> captor = ArgumentCaptor.forClass(
          ProductCategoryCreateRequest.class);

      verify(maintenanceGuideService).registerProductCategory(captor.capture());
      ProductCategoryCreateRequest actual = captor.getValue();
      assertThat(actual.getCategoryName()).isEqualTo("test");
    }

    @Test
    void 商品作成時にIDは自動採番され成功メッセージが返ってくること() throws Exception {
      Product product = new Product();
      product.setCategoryId(1);
      product.setProductName("Test Product");
      product.setDescription("This is a test product.");
      product.setGuideMatchKey("TEST1234");
      product.setPrice(BigDecimal.valueOf(1000.0));

      when(maintenanceGuideService.registerProduct(any(ProductCreateRequest.class)))
          .thenReturn(product);
      String requestBody = """
          {
            "productName": "Test Product",
            "description": "This is a test product.",
            "guideMatchKey": "TEST1234",
            "price": 1000.0
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Product created successfully"));

      ArgumentCaptor<ProductCreateRequest> captor = ArgumentCaptor.forClass(
          ProductCreateRequest.class);

      verify(maintenanceGuideService).registerProduct(captor.capture());
      ProductCreateRequest actual = captor.getValue();
      assertThat(actual.getProductName()).isEqualTo("Test Product");
      assertThat(actual.getDescription()).isEqualTo("This is a test product.");
      assertThat(actual.getGuideMatchKey()).isEqualTo("TEST1234");
      assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(1000.0));
    }

    @Test
    void メンテナンスガイド登録時に既に登録されていた場合エラーをかえすこと() throws Exception {
      when(maintenanceGuideService.registerMaintenanceGuide(
          any(MaintenanceGuideCreateRequest.class)))
          .thenThrow(
              new MaintenanceGuideAlreadyExistsException("Maintenance guide already exists"));
      String requestBody = """
          {
            "make": "toyota",
            "vehicleName": "アクア",
            "model": "TEST1234",
            "type": "EG-1234",
            "startYear": "2000-10",
            "endYear": "2020-01",
            "oilViscosity": "0w-20",
            "oilQuantityWithFilter": 3.5,
            "oilQuantityWithoutFilter": 3.0,
            "oilFilterPartNumber": "12345",
            "carWashSize": "S"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/maintenance-guides")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(jsonPath("$.message").value("Maintenance guide already exists"));

    }

    @Test
    void メンテナンスガイド作成時に空白であった場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "make": "",
            "vehicleName": "",
            "model": "",
            "type": "",
            "startYear": "",
            "endYear": "",
            "oilViscosity": "",
            "oilQuantityWithFilter": null,
            "oilQuantityWithoutFilter": null,
            "oilFilterPartNumber": "",
            "carWashSize": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/maintenance-guides")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'type' && @.message == 'エンジン型式は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'startYear' && @.message == '年式は必須です')]").exists())
          .andExpect(
              jsonPath(
                  "$.errors[?(@.field == 'make' && @.message == 'メーカー名は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'carWashSize' && @.message == '洗車サイズはSS,S,M,L,LL,XLのいずれかを入力してください。')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilViscosity' && @.message == 'オイルの粘度は正しい名前を入力してください。例）0w-20,5w-30等')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'type' && @.message == 'エンジン型式は半角英大文字と数字で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'startYear' && @.message == 'yearはYYYY-MM形式で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'endYear' && @.message == 'yearはYYYY-MM形式で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'vehicleName' && @.message == '車種名は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'endYear' && @.message == '年式は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'model' && @.message == '車体番号は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'model' && @.message == '車体番号は半角英大文字と数字で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'make' && @.message == 'メーカーは半角英小文字で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilFilterPartNumber' && @.message == '純正フィルター型番を必ず入力してください')]").exists());
    }

    @Test
    void 商品カテゴリー作成時に空白であった場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "categoryName": ""
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/product-categories")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'categoryName' && @.message == 'カテゴリー名を入力してください')]").exists())
          .andExpect(jsonPath("$.errors[?(@.message == 'カテゴリー名を入力してください')]").exists());
    }

    @Test
    void 商品作成時に空白であった場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "categoryId": 1,
            "productName": "",
            "description": "",
            "guideMatchKey": "",
            "price": null
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'productName' && @.message == '商品名は必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'guideMatchKey' && @.message == 'ガイドキーは必須です')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'price' && @.message == 'nullは許可されていません')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'description' && @.message == '商品詳細は必須です')]").exists());
    }

    @Test
    void メンテナンスガイド作成時にオイル数量が0以下であった場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "make": "toyota",
            "vehicleName": "アクア",
            "model": "TEST1234",
            "type": "EG-1234",
            "startYear": "2000-10",
            "endYear": "2020-01",
            "oilViscosity": "0w-20",
            "oilQuantityWithFilter": 0.0,
            "oilQuantityWithoutFilter": 0.0,
            "oilFilterPartNumber": "12345",
            "carWashSize": "S"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/maintenance-guides")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilQuantityWithFilter' && @.message == 'オイル量は0より大きい数字で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilQuantityWithoutFilter' && @.message == 'オイル量は0より大きい数字で入力してください')]").exists());
    }

    @Test
    void メンテナンスガイド作成時にオイル数量をMax値を超えていた場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "make": "toyota",
            "vehicleName": "アクア",
            "model": "TEST1234",
            "type": "EG-1234",
            "startYear": "2000-10",
            "endYear": "2020-01",
            "oilViscosity": "0w-20",
            "oilQuantityWithFilter": 100.0,
            "oilQuantityWithoutFilter": 100.0,
            "oilFilterPartNumber": "12345",
            "carWashSize": "S"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/maintenance-guides")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilQuantityWithFilter' && @.message == 'オイル量は整数部2桁、小数部1桁で入力してください')]").exists())
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'oilQuantityWithoutFilter' && @.message == 'オイル量は整数部2桁、小数部1桁で入力してください')]").exists());
    }

    @Test
    void 商品作成時に価格が0以下であった場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "categoryId": 1,
            "productName": "Test Product",
            "description": "This is a test product.",
            "guideMatchKey": "TEST1234",
            "price": -100.0
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'price' && @.message == '価格は０より大きい整数でなければなりません')]").exists());
    }

    @Test
    void 商品作成時に価格が小数点以下を含んでいた場合validationエラーをかえすこと() throws Exception {
      String requestBody = """
          {
            "categoryId": 1,
            "productName": "Test Product",
            "description": "This is a test product.",
            "guideMatchKey": "TEST1234",
            "price": 1000.99
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("validation error"))
          .andExpect(jsonPath(
              "$.errors[?(@.field == 'price' && @.message == '価格は０より大きい整数でなければなりません')]").exists());
    }

    @Test
    void 商品カテゴリー作成時に既に登録されていた場合エラーをかえすこと() throws Exception {
      when(maintenanceGuideService.registerProductCategory(
          any(ProductCategoryCreateRequest.class)))
          .thenThrow(new MasterException.ProductCategoryAlreadyExistsException(
              "Product category already exists"));
      String requestBody = """
          {
            "categoryName": "test"
          }
          """;
      mockMvc.perform(MockMvcRequestBuilders.post("/product-categories")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isUnprocessableEntity())
          .andExpect(jsonPath("$.message").value("Product category already exists"));
    }
  }
}
