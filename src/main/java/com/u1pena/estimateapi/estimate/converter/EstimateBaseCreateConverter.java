package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.entity.EstimateBase;

public class EstimateBaseCreateConverter {

  public static EstimateBase toEntity(int customerId, int vehicleId, int maintenanceId) {
    // ここでは、EstimateBaseのIDを生成するロジックを実装します。
    // 例えば、データベースに保存して生成されたIDを返すなどの処理が考えられます。
    // 今回は仮にcustomerIdとestimateNumberを連結した値を返すとします。
    return EstimateBase.builder()
        .customerId(customerId)
        .vehicleId(vehicleId)
        .maintenanceId(maintenanceId)
        .estimateDate(java.time.LocalDate.now())
        .build();
  }
}
