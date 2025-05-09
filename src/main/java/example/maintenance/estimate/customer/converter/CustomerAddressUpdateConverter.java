package example.maintenance.estimate.customer.converter;

import example.maintenance.estimate.customer.dto.request.CustomerAddressUpdateRequest;
import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.enums.Prefecture;

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
