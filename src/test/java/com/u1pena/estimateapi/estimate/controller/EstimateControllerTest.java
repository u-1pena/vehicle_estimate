package com.u1pena.estimateapi.estimate.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleNotFoundException;
import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import com.u1pena.estimateapi.customer.helper.CustomizedMockMvc;
import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductCreateRequest;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.exception.EstimateException.EstimateBaseNotFoundException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.EstimateProductNotFoundException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.NoMatchMaintenanceGuideException;
import com.u1pena.estimateapi.estimate.helper.EstimateTestHelper;
import com.u1pena.estimateapi.estimate.service.EstimateService;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(EstimateController.class)
@Import(CustomizedMockMvc.class)
class EstimateControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  EstimateService estimateService;
  EstimateTestHelper estimateTestHelper;
  CustomerTestHelper customerTestHelper;

  @BeforeEach
  void setUp() {
    estimateTestHelper = new EstimateTestHelper();
    customerTestHelper = new CustomerTestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 見積もりベースIDから顧客情報と見積もりベースと詳細で閲覧できる() throws Exception {
      // テスト用のデータを作成
      EstimateHeaderResponse estimateHeaderResponse = estimateTestHelper.estimateHeaderResponseMock();
      List<EstimateProductResponse> estimateProducts = estimateTestHelper.estimateProductResponseMock();

      BigDecimal totalPrice = BigDecimal.valueOf(7700.00);
      EstimateFullResponse fullResponse = EstimateFullResponse.builder()
          .estimateHeader(estimateHeaderResponse)
          .estimateProducts(List.of(estimateProducts.get(0), estimateProducts.get(1)))
          .totalPrice(totalPrice)
          .build();
      // 準備
      doReturn(fullResponse).when(estimateService).getEstimateFullById(1);
      mockMvc.perform(MockMvcRequestBuilders.get("/estimates/full/{estimateId}", 1))
          .andExpect(status().isOk())
          .andExpect(MockMvcResultMatchers.content().json(
              """
                  {
                  "estimateHeader": {
                          "estimateBaseId": 1,
                          "estimateDate": "2025-01-01",
                          "customer": {
                              "fullName": "suzuki ichiro",
                              "fullNameKana": "ｽｽﾞｷ ｲﾁﾛｳ",
                              "email": "ichiro@example.com",
                              "phoneNumber": "090-1234-5678"
                          },
                          "customerAddress": {
                              "postalCode": "123-4567",
                              "fullAddress": "東京都 港区 六本木1-1-1 都心ビル101号室"
                          },
                          "vehicle": {
                              "vehicleName": "カローラアクシオ",
                              "make": "toyota",
                              "model": "DBA-NZE141",
                              "type": "1NZ",
                              "year": "2010-12"
                          }
                      },
                      "estimateProducts": [
                          {
                              "categoryName": "motor_oil",
                              "productName": "ハイグレードオイル_0w-20",
                              "quantity": 3.4,
                              "unitPrice": 2000.00,
                              "totalPrice": 6800.00
                          },
                          {
                              "categoryName": "oil_filter",
                              "productName": "オイルフィルター",
                              "quantity": 1.0,
                              "unitPrice": 900.00,
                              "totalPrice": 900.00
                              }
                     ],
                      "totalPrice": 7700.00
                  }
                  """, true));
    }

    @Test
    void 顧客情報から見積もり概要をすべて閲覧できる() throws Exception {
      //準備
      EstimateSummaryResult estimateSummaryResult = EstimateSummaryResult.builder()
          .estimateBaseId(1)
          .estimateDate(LocalDate.of(2025, 6, 18))
          .vehicleName("カローラアクシオ")
          .estimateSummary("motor_oil")
          .totalPrice(BigDecimal.valueOf(12000.00))
          .build();
      EstimateSummaryResult estimateSummaryResult2 = EstimateSummaryResult.builder()
          .estimateBaseId(2)
          .estimateDate(LocalDate.of(2025, 7, 18))
          .vehicleName("カローラアクシオ")
          .estimateSummary("car_wash")
          .totalPrice(BigDecimal.valueOf(3300.00))
          .build();

      doReturn(List.of(estimateSummaryResult, estimateSummaryResult2)).when(estimateService)
          .getEstimateSummaryByCustomerId(1);
      mockMvc.perform(MockMvcRequestBuilders.get("/estimates/customers/{customerId}", 1))
          .andExpect(status().isOk())
          .andExpect(content().json(
              """
                  [
                      {
                          "estimateBaseId": 1,
                          "estimateDate": "2025-06-18",
                          "vehicleName": "カローラアクシオ",
                          "estimateSummary": "motor_oil",
                          "totalPrice": 12000.00
                      },
                      {
                          "estimateBaseId": 2,
                          "estimateDate": "2025-07-18",
                          "vehicleName": "カローラアクシオ",
                          "estimateSummary": "car_wash",
                          "totalPrice": 3300.00
                      }
                  ]
                  """
          ));
    }

    @Test
    void 見積もりを日付で期間を指定して検索できる() throws Exception {
      // 準備
      EstimateSummaryResult estimateSummaryResult1 = EstimateSummaryResult.builder()
          .estimateBaseId(1)
          .estimateDate(LocalDate.of(2025, 1, 1))
          .customerName("suzuki ichiro")
          .vehicleName("カローラアクシオ")
          .estimateSummary("carWash ・ motor_oil ・ oil_filter")
          .totalPrice(BigDecimal.valueOf(10500.00))
          .build();
      EstimateSummaryResult estimateSummaryResult2 = EstimateSummaryResult.builder()
          .estimateBaseId(11)
          .estimateDate(LocalDate.of(2025, 6, 20))
          .customerName("suzuki ichiro")
          .vehicleName("カローラアクシオ")
          .estimateSummary(null)
          .totalPrice(BigDecimal.ZERO)
          .build();
      doReturn(List.of(estimateSummaryResult1, estimateSummaryResult2))
          .when(estimateService)
          .getEstimatesByDateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
      mockMvc.perform(MockMvcRequestBuilders.get("/estimates")
              .param("startDate", "2025-01-01")
              .param("endDate", "2025-12-31"))
          .andExpect(status().isOk())
          .andExpect(content().json(
              """
                  [
                      {
                          "estimateDate": "2025-01-01",
                          "estimateBaseId": 1,
                          "customerName": "suzuki ichiro",
                          "vehicleName": "カローラアクシオ",
                          "estimateSummary": "carWash ・ motor_oil ・ oil_filter",
                          "totalPrice": 10500.0
                      },
                      {
                          "estimateDate": "2025-06-20",
                          "estimateBaseId": 11,
                          "customerName": "suzuki ichiro",
                          "vehicleName": "カローラアクシオ",
                          "estimateSummary": null,
                          "totalPrice": 0.0
                      }
                  ]
                  """, true));
    }
  }

  @Nested
  class CreateClass {

    @Test
    void 見積もりベースを作成できる() throws Exception {
      int newEstimateBaseId = 1;
      //準備
      String requestBody = """
          {
            "vehicleId": 1
          }
          """;
      when(estimateService.registerEstimateBase(1)).thenReturn(newEstimateBaseId);
      //実行
      mockMvc.perform(MockMvcRequestBuilders.post("/estimates")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(jsonPath("$.message").value("Estimate created successfully"))
          .andExpect(
              header().string("Location", endsWith("estimates/" + newEstimateBaseId)));
    }

    @Test
    void 見積もりベースを作成する際車両情報がない場合は400エラーをスローする() throws Exception {
      // 準備
      String requestBody = """
          {
            "vehicleId": 0
          }
          """;
      VehicleNotFoundException exception = new VehicleNotFoundException("Vehicle not found");
      doThrow(exception)
          .when(estimateService).registerEstimateBase(0);
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.post("/estimates")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void 見積もり詳細を作成できる() throws Exception {
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .estimateBaseId(1)
          .productId(1)
          .quantity(2.0)
          .unitPrice(new BigDecimal("1000.00"))
          .totalPrice(new BigDecimal("2000.00"))
          .build();
      String requestBody = """
          {
            "productId": 1
          }
           """;
      doNothing().when(estimateService)
          .registerEstimateProduct(eq(1), any(EstimateProductCreateRequest.class));
      mockMvc.perform(MockMvcRequestBuilders.post("/estimates/1/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Estimate product created successfully"))
          .andExpect(header().string("Location", endsWith("estimates/1/products")));
    }

    @Test
    void 見積もり詳細を作成する際にガイドに商品IDがない場合は400エラーをスローする() throws Exception {
      // 準備
      String requestBody = """
          {
            "productId": 0
          }
          """;
      NoMatchMaintenanceGuideException exception = new NoMatchMaintenanceGuideException();
      doThrow(exception)
          .when(estimateService)
          .registerEstimateProduct(eq(1), any(EstimateProductCreateRequest.class));
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.post("/estimates/1/products")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(exception.getMessage()));
    }
  }

  @Nested
  class DeleteClass {

    @Test
    void 見積もりベースを削除できる() throws Exception {
      // 準備
      doNothing().when(estimateService).deleteEstimateBase(1);
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.delete("/estimates/{estimateId}", 1))
          .andExpect(status().isNoContent());
    }

    @Test
    void 見積もりベースが存在しない場合は404エラーをスローする() throws Exception {
      // 準備
      EstimateBaseNotFoundException exception = new EstimateBaseNotFoundException();
      doThrow(exception)
          .when(estimateService).deleteEstimateBase(999);
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.delete("/estimates/{estimateId}", 999))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void 見積もり詳細を１件削除できる() throws Exception {
      // 準備
      doNothing().when(estimateService).deleteEstimateProduct(1);
      // 実行
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/estimates/products/{productId}", 1))
          .andExpect(status().isNoContent());
    }

    @Test
    void 見積もり詳細が存在しない場合は404エラーをスローする() throws Exception {
      // 準備
      EstimateProductNotFoundException exception = new EstimateProductNotFoundException();
      doThrow(exception)
          .when(estimateService).deleteEstimateProduct(999);
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.delete("/estimates/products/{productId}", 999))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void 見積もりベースに紐づく商品見積もりをすべて削除できる() throws Exception {
      // 準備
      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      doNothing().when(estimateService)
          .deleteEstimateProductsAllByEstimateBaseId(estimateBase.getEstimateBaseId());
      // 実行
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/estimates/{estimateBaseId}/products", 1))
          .andExpect(status().isNoContent());
    }
  }

  @Nested
  class UpdateClass {

    @Test
    void 見積もり詳細が更新できる() throws Exception {
      // 準備
      String requestBody = """
          {
            "quantity": 2.0,
            "unitPrice": 1000.00
          }
          """;
      doNothing().when(estimateService)
          .updateEstimateProduct(eq(1), any(EstimateProductUpdateRequest.class));
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.put("/estimates/products/{estimateProductId}", 1)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNoContent());

    }

    @Test
    void 見積もり詳細が存在しない場合は404エラーをスローする() throws Exception {
      // 準備
      String requestBody = """
          {
            "quantity": 2.0,
            "unitPrice": 1000.00
          }
          """;
      EstimateProductNotFoundException exception = new EstimateProductNotFoundException();
      doThrow(exception)
          .when(estimateService)
          .updateEstimateProduct(eq(999), any(EstimateProductUpdateRequest.class));
      // 実行
      mockMvc.perform(MockMvcRequestBuilders.put("/estimates/products/{estimateProductId}", 999)
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(
              jsonPath("$.message").value(exception.getMessage()));
    }
  }
}
