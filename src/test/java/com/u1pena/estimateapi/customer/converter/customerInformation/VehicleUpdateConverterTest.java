package com.u1pena.estimateapi.customer.converter.customerInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.customer.converter.VehicleUpdateConverter;
import com.u1pena.estimateapi.customer.dto.request.VehicleUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import org.junit.jupiter.api.Test;

class VehicleUpdateConverterTest {

  @Test
  void vehicleUpdateRequestをvehicleに変換できること() {
    // 準備
    Vehicle vehicle = new Vehicle();
    VehicleUpdateRequest vehicleUpdateRequest = new VehicleUpdateRequest();
    vehicleUpdateRequest.setPlateRegion("相模");
    vehicleUpdateRequest.setPlateCategoryNumber("123");
    vehicleUpdateRequest.setPlateHiragana("あ");
    vehicleUpdateRequest.setPlateVehicleNumber("1234");
    vehicleUpdateRequest.setMake("honda");
    vehicleUpdateRequest.setModel("JF1");
    vehicleUpdateRequest.setType("DBA-JF1");
    vehicleUpdateRequest.setYear("2023-10");
    vehicleUpdateRequest.setInspectionDate("2024-10-01");
    vehicleUpdateRequest.setActive(true);
    // 実行
    VehicleUpdateConverter.vehicleUpdateConvertToEntity(vehicle, vehicleUpdateRequest);

    // 検証
    assertEquals(PlateRegion.相模, vehicle.getPlateRegion());
    assertEquals("123", vehicle.getPlateCategoryNumber());
    assertEquals("あ", vehicle.getPlateHiragana());
    assertEquals("1234", vehicle.getPlateVehicleNumber());
    assertEquals("honda", vehicle.getMake());
    assertEquals("JF1", vehicle.getModel());
    assertEquals("DBA-JF1", vehicle.getType());
    assertEquals("2023-10", vehicle.getYear().toString());
    assertEquals("2024-10-01", vehicle.getInspectionDate().toString());
    assertTrue(vehicle.isActive());
  }
}
