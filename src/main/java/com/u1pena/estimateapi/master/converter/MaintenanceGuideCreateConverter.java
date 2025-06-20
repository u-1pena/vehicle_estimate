package com.u1pena.estimateapi.master.converter;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.master.dto.request.MaintenanceGuideCreateRequest;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import java.time.YearMonth;

public class MaintenanceGuideCreateConverter {

  public static MaintenanceGuide toEntity(MaintenanceGuideCreateRequest
      maintenanceGuideCreateRequest) {
    return MaintenanceGuide.builder()
        .make(maintenanceGuideCreateRequest.getMake())
        .vehicleName(maintenanceGuideCreateRequest.getVehicleName())
        .model(maintenanceGuideCreateRequest.getModel())
        .type(maintenanceGuideCreateRequest.getType())
        .startYear(YearMonth.parse(maintenanceGuideCreateRequest.getStartYear()))
        .endYear(YearMonth.parse(maintenanceGuideCreateRequest.getEndYear()))
        .oilViscosity(maintenanceGuideCreateRequest.getOilViscosity())
        .oilQuantityWithFilter(maintenanceGuideCreateRequest.getOilQuantityWithFilter())
        .oilQuantityWithoutFilter(maintenanceGuideCreateRequest.getOilQuantityWithoutFilter())
        .oilFilterPartNumber(maintenanceGuideCreateRequest.getOilFilterPartNumber())
        .carWashSize(CarWashSize.valueOf(maintenanceGuideCreateRequest.getCarWashSize()))
        .build();
  }
}
