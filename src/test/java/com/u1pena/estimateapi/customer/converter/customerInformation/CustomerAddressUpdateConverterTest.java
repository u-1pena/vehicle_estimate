package com.u1pena.estimateapi.customer.converter.customerInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.u1pena.estimateapi.customer.converter.CustomerAddressUpdateConverter;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import org.junit.jupiter.api.Test;

class CustomerAddressUpdateConverterTest {

  @Test
  void customerAddressUpdateRequestをCustomerAddressに変換できること() {
    // 準備
    Customer customer = new Customer();
    customer.setCustomerId(1);
    CustomerAddress customerAddress = new CustomerAddress();
    customerAddress.setAddressId(1);
    CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest();
    customerAddressUpdateRequest.setPostalCode("123-4567");
    customerAddressUpdateRequest.setPrefecture("東京都");
    customerAddressUpdateRequest.setCity("渋谷区");
    customerAddressUpdateRequest.setTownAndNumber("渋谷1-2-3");
    customerAddressUpdateRequest.setBuildingNameAndRoomNumber("渋谷ビル101");

    // 実行
    CustomerAddressUpdateConverter.customerAddressUpdateConvertToEntity(customer, customerAddress,
        customerAddressUpdateRequest);

    // 検証
    assertEquals(customer.getCustomerId(), customerAddress.getCustomerId());
    assertEquals(customerAddress.getPostalCode(), "123-4567");
    assertEquals(customerAddress.getPrefecture().name(), "東京都");
    assertEquals(customerAddress.getCity(), "渋谷区");
    assertEquals(customerAddress.getTownAndNumber(), "渋谷1-2-3");
    assertEquals(customerAddress.getBuildingNameAndRoomNumber(), "渋谷ビル101");
  }
}
