package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressCreateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;

public final class CustomerAddressCreateConverter {

  private CustomerAddressCreateConverter() {
    // プライベートコンストラクタでインスタンス化を防止
  }

  public static CustomerAddress customerAddressConvertToEntity(Customer customer,
      CustomerAddressCreateRequest customerAddressCreateRequest) {
    return CustomerAddress.builder()
        .addressId(customer.getCustomerId())
        .customerId(customer.getCustomerId())
        .postalCode(customerAddressCreateRequest.getPostalCode())
        .prefecture(Prefecture.valueOf(customerAddressCreateRequest.getPrefecture()))
        .city(customerAddressCreateRequest.getCity())
        .townAndNumber(customerAddressCreateRequest.getTownAndNumber())
        .buildingNameAndRoomNumber(customerAddressCreateRequest.getBuildingNameAndRoomNumber())
        .build();
  }
}
