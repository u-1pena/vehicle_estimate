package yuichi.car.estimate.management.converter;

import yuichi.car.estimate.management.dto.request.CustomerAddressUpdateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.enums.Prefecture;

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
