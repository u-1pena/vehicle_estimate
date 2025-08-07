package com.u1pena.estimateapi.master.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.master.dto.request.MaintenanceGuideCreateRequest;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class MaintenanceGuideCreateConverterTest {

  @Test
  void maintenanceCreateRequestをmaintenanceに変換できる() {

    // Arrange
    MaintenanceGuideCreateRequest maintenanceGuideCreateRequest =
        new MaintenanceGuideCreateRequest();
    maintenanceGuideCreateRequest.setMake("toyota");
    maintenanceGuideCreateRequest.setVehicleName("プリウス");
    maintenanceGuideCreateRequest.setModel("ZVW30");
    maintenanceGuideCreateRequest.setType("1ZV-FXE");
    maintenanceGuideCreateRequest.setStartYear("2009-10");
    maintenanceGuideCreateRequest.setEndYear("2015-12");
    maintenanceGuideCreateRequest.setOilViscosity("0w-20");
    maintenanceGuideCreateRequest.setOilQuantityWithoutFilter(4.5);
    maintenanceGuideCreateRequest.setOilQuantityWithFilter(4.8);
    maintenanceGuideCreateRequest.setOilFilterPartNumber("TEST");
    maintenanceGuideCreateRequest.setCarWashSize("M");

    // Act
    MaintenanceGuide actual = MaintenanceGuideCreateConverter.toEntity(
        maintenanceGuideCreateRequest);

    // Assert
    assertThat(actual.getMake()).isEqualTo("toyota");
    assertThat(actual.getVehicleName()).isEqualTo("プリウス");
    assertThat(actual.getModel()).isEqualTo("ZVW30");
    assertThat(actual.getType()).isEqualTo("1ZV-FXE");
    assertThat(actual.getStartYear()).isEqualTo(YearMonth.parse("2009-10"));
    assertThat(actual.getEndYear()).isEqualTo(YearMonth.parse("2015-12"));
    assertThat(actual.getOilViscosity()).isEqualTo("0w-20");
    assertThat(actual.getOilQuantityWithoutFilter()).isEqualTo(4.5);
    assertThat(actual.getOilQuantityWithFilter()).isEqualTo(4.8);
    assertThat(actual.getOilFilterPartNumber()).isEqualTo("TEST");
    assertThat(actual.getCarWashSize()).isEqualTo(CarWashSize.valueOf("M"));
  }
}
