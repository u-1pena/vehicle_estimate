package com.u1pena.estimateapi.estimate.helper;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class EstimateTestHelper {

  public List<EstimateBase> estimateBaseMocks() {
    return List.of(new EstimateBase(1, 1, 1, 1,
            LocalDate.of(2025, 1, 1)),
        new EstimateBase(2, 2, 2, 2,
            LocalDate.of(2025, 2, 1)),
        new EstimateBase(3, 3, 3, 3,
            LocalDate.of(2025, 3, 1)));
  }

  public List<EstimateProduct> estimateProductMock() {
    return List.of(new EstimateProduct(1, 1, 1,
        1.0, BigDecimal.valueOf(1800.00).setScale(2), BigDecimal.valueOf(1800.00).setScale(2)));
  }

  public List<MaintenanceGuide> maintenanceGuideMock() {
    return List.of(new MaintenanceGuide(1, "toyota", "カローラアクシオ",
        "DBA-NZE141", "1NZ", YearMonth.of(2006, 10),
        YearMonth.of(2012, 5), "0w-20", 3.7,
        3.4, "90915-10003", CarWashSize.M));
  }

  public CustomerResponse customerResponseMock() {
    CustomerResponse customerResponse = CustomerResponse.builder()
        .fullName("suzuki ichiro")
        .fullNameKana("スズキ イチロウ")
        .email("ichiro@example.com")
        .phoneNumber("090-1234-5678")
        .build();
    return customerResponse;
  }

  public CustomerAddressResponse customerAddressResponseMock() {
    CustomerAddressResponse customerAddressResponse = CustomerAddressResponse.builder()
        .postalCode("123-4567")
        .fullAddress("東京都 港区 六本木1-1-1 都心ビル101号室")
        .build();
    return customerAddressResponse;
  }

  public VehicleResponse vehicleResponseMock() {
    VehicleResponse vehicleResponse = VehicleResponse.builder()
        .make("toyota")
        .vehicleName("カローラアクシオ")
        .model("DBA-NZE141")
        .type("1NZ")
        .year(YearMonth.of(2010, 12))
        .build();
    return vehicleResponse;
  }

  public EstimateHeaderResponse estimateHeaderResponseMock() {
    return EstimateHeaderResponse.builder()
        .estimateBaseId(1)
        .estimateDate(LocalDate.of(2025, 1, 1))
        .customer(customerResponseMock())
        .customerAddress(customerAddressResponseMock())
        .vehicle(vehicleResponseMock())
        .build();
  }

  public List<EstimateProductResponse> estimateProductResponseMock() {
    return List.of(EstimateProductResponse.builder()
            .categoryName("motor_oil")
            .productName("ハイグレードオイル_0w-20")
            .quantity(3.4)
            .unitPrice(new BigDecimal("2000"))
            .totalPrice(new BigDecimal("6800"))
            .build(),
        EstimateProductResponse.builder()
            .categoryName("oil_filter")
            .productName("オイルフィルター")
            .quantity(1)
            .unitPrice(new BigDecimal("900"))
            .totalPrice(new BigDecimal("900"))
            .build());
  }

  public List<EstimateProduct> estimateProductsMock() {
    return List.of(
        EstimateProduct.builder()
            .estimateBaseId(1)
            .productId(1)
            .quantity(3.4)
            .unitPrice(new BigDecimal("2000"))
            .totalPrice(new BigDecimal("6800"))
            .build(),
        EstimateProduct.builder()
            .estimateBaseId(1)
            .productId(2)
            .quantity(1)
            .unitPrice(new BigDecimal("900"))
            .totalPrice(new BigDecimal("900"))
            .build()
    );
  }

  public List<EstimateProductJoinResult> joinResultsMock() {
    return List.of(
        EstimateProductJoinResult.builder()
            .productId(1)
            .productName("ハイグレードオイル_0w-20")
            .categoryName("motor_oil")
            .build(),
        EstimateProductJoinResult.builder()
            .productId(2)
            .productName("オイルフィルター")
            .categoryName("oil_filter")
            .build()
    );
  }

  public EstimateBase estimateBaseMock() {
    return EstimateBase.builder()
        .estimateBaseId(1)
        .estimateDate(LocalDate.of(2025, 1, 1))
        .customerId(1)
        .vehicleId(1)
        .build();
  }

  public GuideProductPermission guideProductPermissionMock() {
    return GuideProductPermission.builder()
        .maintenanceId(1)
        .productId(1)
        .categoryId(1) // CATEGORY_OIL
        .quantity(3.0)
        .autoAdjustQuantity(true)
        .build();
  }
}
