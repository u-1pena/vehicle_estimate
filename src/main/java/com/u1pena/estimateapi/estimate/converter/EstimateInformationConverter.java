package com.u1pena.estimateapi.estimate.converter;

import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import java.time.LocalDate;

public class EstimateInformationConverter {

  public static EstimateHeaderResponse toDto(int estimateBaseId, LocalDate estimateDate,
      CustomerResponse customer,
      CustomerAddressResponse customerAddress, VehicleResponse vehicle) {
    return EstimateHeaderResponse.builder()
        .estimateBaseId(estimateBaseId)
        .estimateDate(estimateDate)
        .customer(customer)
        .customerAddress(customerAddress)
        .vehicle(vehicle)
        .build();
  }

}
