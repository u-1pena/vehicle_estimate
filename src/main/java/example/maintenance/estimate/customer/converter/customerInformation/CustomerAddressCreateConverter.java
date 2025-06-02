package example.maintenance.estimate.customer.converter.customerInformation;

import example.maintenance.estimate.customer.dto.request.customerInformation.CustomerAddressCreateRequest;
import example.maintenance.estimate.customer.entity.customerInformation.Customer;
import example.maintenance.estimate.customer.entity.customerInformation.CustomerAddress;
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
