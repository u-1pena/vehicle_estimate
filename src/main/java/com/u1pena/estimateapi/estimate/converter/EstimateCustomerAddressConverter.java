package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;

public class EstimateCustomerAddressConverter {

  public static CustomerAddressResponse toDto(CustomerAddress customerAddress) {
    String fullAddress =
        customerAddress.getPrefecture() + " " +
            customerAddress.getCity() + " " +
            customerAddress.getTownAndNumber() + " " +
            customerAddress.getBuildingNameAndRoomNumber();

    return CustomerAddressResponse.builder()
        .postalCode(customerAddress.getPostalCode())
        .fullAddress(fullAddress)
        .build();
  }
}
