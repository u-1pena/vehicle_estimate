package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class EstimateHeaderConverterTest {

  @Test
  void 見積もりヘッダーをDTOに変換する() {
    // 準備
    int estimateBaseId = 1;
    LocalDate estimateDate = LocalDate.of(2023, 10, 1);
    CustomerResponse customer = CustomerResponse.builder()
        .fullName("tanaka tarou")
        .fullNameKana("ﾀﾅｶﾀﾛｳ")
        .email("tarou@example.com")
        .phoneNumber("090-1234-5678")
        .build();

    CustomerAddressResponse customerAddress = CustomerAddressResponse.builder()
        .fullAddress("123-4567 東京都新宿区西新宿2-8-1 新宿ビル101号室")
        .build();
    VehicleResponse vehicle = VehicleResponse.builder()
        .vehicleName("プリウス")
        .make("toyota")
        .model("ZVW30")
        .type("2ZR-FXE")
        .year(YearMonth.of(2010, 1))
        .build();

    // 実行
    EstimateHeaderResponse actual = EstimateHeaderConverter.toDto(
        estimateBaseId, estimateDate, customer, customerAddress, vehicle);

    // 検証
    assertThat(actual).isNotNull();
    assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBaseId);
    assertThat(actual.getEstimateDate()).isEqualTo(estimateDate);
    assertThat(actual.getCustomer()).isEqualTo(customer);
    assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
    assertThat(actual.getVehicle()).isEqualTo(vehicle);
  }

}
