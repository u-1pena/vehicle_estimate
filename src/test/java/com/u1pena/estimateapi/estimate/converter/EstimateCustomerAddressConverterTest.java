package com.u1pena.estimateapi.estimate.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import org.junit.jupiter.api.Test;

class EstimateCustomerAddressConverterTest {

  @Test
  void toDtoメソッドが正しくCustomerAddressをCustomerAddressResponseに変換すること() {
    // 準備
    CustomerAddress customerAddress = new CustomerAddress();
    customerAddress.setPostalCode("123-4567");
    customerAddress.setPrefecture(Prefecture.東京都);
    customerAddress.setCity("新宿区");
    customerAddress.setTownAndNumber("西新宿2-8-1");
    customerAddress.setBuildingNameAndRoomNumber("新宿ビル 101号室");

    // 実行
    CustomerAddressResponse response = EstimateCustomerAddressConverter.toDto(customerAddress);

    // 検証
    assertEquals("123-4567", response.getPostalCode());
    assertEquals("東京都 新宿区 西新宿2-8-1 新宿ビル 101号室", response.getFullAddress());
  }

}
