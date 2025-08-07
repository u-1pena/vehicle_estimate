package com.u1pena.estimateapi.estimate.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstimateFullConverterTest {

  CustomerTestHelper customerTestHelper;

  @BeforeEach
  void setup() {
    customerTestHelper = new CustomerTestHelper();
  }

  @Test
  void estimateHeaderとestimateProductsとtotalPriceを使ってEstimateFullResponseを生成できる() {
    // 準備
    CustomerResponse customerResponse = CustomerResponse.builder()
        .fullName("yamada tarou")
        .fullNameKana("ﾔﾏﾀﾞ ﾀﾛｳ")
        .email("tarou@exaple.com")
        .phoneNumber("090-1234-5678")
        .build();

    CustomerAddressResponse customerAddressResponse = CustomerAddressResponse.builder()
        .postalCode("123-4567")
        .fullAddress("東京都 新宿区 西新宿2-8-1 新宿ビル 101号室")
        .build();
    VehicleResponse vehicleResponse = VehicleResponse.builder()
        .vehicleName("トヨタ プリウス")
        .make("toyota")
        .type("2ZR-FXE")
        .model("ZVW30")
        .year(YearMonth.of(2010, 1))
        .build();
    EstimateHeaderResponse estimateHeaderResponse = EstimateHeaderResponse.builder()
        .estimateBaseId(1)
        .estimateDate(LocalDate.of(2023, 10, 1))
        .customer(customerResponse) // CustomerResponseはテストではnullで良い
        .customerAddress(customerAddressResponse) // CustomerAddressResponseも同様にnull
        .vehicle(vehicleResponse) // VehicleResponseも同様にnull
        .build();

    EstimateProductResponse estimateProduct = EstimateProductResponse.builder()
        .categoryName("Test Category")
        .productName("Test Product")
        .quantity(2)
        .unitPrice(BigDecimal.valueOf(1000))
        .build();
    List<EstimateProductResponse> estimateProducts = List.of(estimateProduct);
    BigDecimal totalPrice = BigDecimal.valueOf(2000);

    // 実行
    EstimateFullResponse response = EstimateFullConverter.toDto(estimateHeaderResponse,
        estimateProducts, totalPrice);

    // 検証
    assertNotNull(response);
    assertEquals(estimateHeaderResponse, response.getEstimateHeader());
    assertEquals(estimateProducts, response.getEstimateProducts());
    assertEquals(totalPrice, response.getTotalPrice());
  }
}
