package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class EstimateVehicleConverterTest {

  @Test
  void VehicleNameとVehicleEntitiyを見積もり用のVehicleResponseに変換できる() {
    // 準備
    String vehicleName = "プリウス";
    Vehicle vehicle = Vehicle.builder()
        .make("toyota")
        .model("ZVW30")
        .type("2ZR-FXE")
        .year(YearMonth.of(2010, 1))
        .build();

    // 実行
    VehicleResponse actual = EstimateVehicleConverter.toDto(vehicleName, vehicle);

    // 検証
    assertThat(actual.getVehicleName()).isEqualTo(vehicleName);
    assertThat(actual.getMake()).isEqualTo(vehicle.getMake());
    assertThat(actual.getModel()).isEqualTo(vehicle.getModel());
    assertThat(actual.getType()).isEqualTo(vehicle.getType());
    assertThat(actual.getYear()).isEqualTo(vehicle.getYear());
  }
}
