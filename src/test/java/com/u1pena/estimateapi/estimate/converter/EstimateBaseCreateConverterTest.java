package com.u1pena.estimateapi.estimate.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.estimate.dto.request.EstimateBaseCreateRequest;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import org.junit.jupiter.api.Test;

class EstimateBaseCreateConverterTest {

  @Test
  void EstimateBaseCreateRequestからEstimateBaseに変換できること() {
    // 準備
    EstimateBaseCreateRequest estimateBaseCreateRequest = EstimateBaseCreateRequest.builder()
        .customerId(1)
        .vehicleId(1)
        .maintenanceId(1)
        .build();

    // 実行
    EstimateBase actual = EstimateBaseCreateConverter.toEntity(
        estimateBaseCreateRequest.getCustomerId(),
        estimateBaseCreateRequest.getVehicleId(), estimateBaseCreateRequest.getMaintenanceId());
    // 検証
    assertThat(actual.getCustomerId()).isEqualTo(1);
    assertThat(actual.getVehicleId()).isEqualTo(1);
    assertThat(actual.getMaintenanceId()).isEqualTo(1);
  }
}
