package com.u1pena.estimateapi.customer.converter;

import com.u1pena.estimateapi.common.enums.Prefecture;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;

public class CustomerAddressUpdateConverter {

  public static void customerAddressUpdateConvertToEntity(Customer customer,
      CustomerAddress customerAddress,
      CustomerAddressUpdateRequest customerAddressUpdateRequest) {
    customerAddress.setCustomerId(customer.getCustomerId());
    customerAddress.setPrefecture(Prefecture.valueOf(customerAddressUpdateRequest.getPrefecture()));
    customerAddress.setPostalCode(customerAddressUpdateRequest.getPostalCode());
    customerAddress.setCity(customerAddressUpdateRequest.getCity());
    customerAddress.setTownAndNumber(customerAddressUpdateRequest.getTownAndNumber());
    customerAddress.setBuildingNameAndRoomNumber(
        customerAddressUpdateRequest.getBuildingNameAndRoomNumber());
  }
}
