package yuichi.car.estimate.management.converter;

import yuichi.car.estimate.management.dto.request.CustomerAddressCreateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.enums.Prefecture;

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
