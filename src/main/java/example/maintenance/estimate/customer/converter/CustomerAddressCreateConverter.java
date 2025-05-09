package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.request.CustomerAddressCreateRequest;
import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.enums.Prefecture;

public class CustomerAddressCreateConverter {


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
