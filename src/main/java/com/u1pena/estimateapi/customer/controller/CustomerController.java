package com.u1pena.estimateapi.customer.controller;

import com.u1pena.estimateapi.common.response.GlobalResponse;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerAddressUpdateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerInformationRequest;
import com.u1pena.estimateapi.customer.dto.request.CustomerUpdateRequest;
import com.u1pena.estimateapi.customer.dto.request.VehicleCreateRequest;
import com.u1pena.estimateapi.customer.dto.request.VehicleUpdateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.service.CustomerService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CustomerController {

  private final CustomerService customerService;


  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  /*顧客情報を全て取得する処理
  @RequestParamでname,kana,emailを受け取り、handleRequestParamでそれぞれの値を受け取り、
  それぞれの値がnullでない場合、それぞれの値をキーにして検索を行う*/
  @GetMapping("/customers")
  public List<CustomerInformationRequest> findCustomersByRequestParam(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "kana", required = false) String kana,
      @RequestParam(value = "email", required = false) String email) {
    List<CustomerInformationRequest> result = customerService.findCustomerInformation(name, kana,
        email);
    return result;
  }

  /*
  車両ナンバー４桁から顧客情報を検索する処理
   */
  @GetMapping("/vehicles/numbers/{plateVehicleNumber}")
  public List<CustomerInformationRequest> findCustomersByPlateVehicleNumber(
      @PathVariable String plateVehicleNumber) {
    List<CustomerInformationRequest> result = customerService.findInformationByPlateVehicleNumber(
        plateVehicleNumber);
    return result;
  }

  /*
  電話番号から顧客情報を検索する処理
  0x0-xxxx-xxxxの形式で検索を行う
   */
  @GetMapping("customers/phone/{phoneNumber}")
  public List<CustomerInformationRequest> searchUsersByPhoneNumber(
      @PathVariable String phoneNumber) {
    List<CustomerInformationRequest> result = customerService.findCustomerInformationByPhoneNumber(
        phoneNumber);
    return result;
  }

  /*
  顧客情報を登録する処理
   */
  @PostMapping("/customers")
  public ResponseEntity<GlobalResponse> createCustomer(
      @RequestBody @Valid CustomerCreateRequest customerCreateRequest,
      UriComponentsBuilder uriBuilder) {
    Customer customer = customerService.registerCustomer(customerCreateRequest);
    URI location = uriBuilder.path("/customers/{customerId}")
        .buildAndExpand(customer.getCustomerId())
        .toUri();
    GlobalResponse body = new GlobalResponse("Customer created");
    return ResponseEntity.created(location).body(body);
  }

  /*
  住所情報を登録する処理
   */
  @PostMapping("customers/{customerId}/addresses")
  public ResponseEntity<GlobalResponse> createUserDetail(
      @PathVariable("customerId") int customerId,
      @RequestBody @Valid CustomerAddressCreateRequest customerAddressCreateRequest, // 専用リクエストクラス
      UriComponentsBuilder uriBuilder) {
    CustomerAddress customerAddress = customerService.registerCustomerAddress(customerId,
        customerAddressCreateRequest);
    URI location = uriBuilder.path("customers/{customerId}/addresses/{addressId}")
        .buildAndExpand(customerId, customerAddress.getAddressId()).toUri();
    GlobalResponse body = new GlobalResponse("CustomerAddress Created");
    return ResponseEntity.created(location).body(body);
  }

  /*
  車両情報を登録する処理
   */
  @PostMapping("vehicles/{customerId}")
  public ResponseEntity<GlobalResponse> createVehicle(
      @PathVariable("customerId") int customerId,
      @RequestBody @Valid VehicleCreateRequest vehicleCreateRequest,
      UriComponentsBuilder uriBuilder) {
    Vehicle vehicle = customerService.registerVehicle(customerId, vehicleCreateRequest);
    URI location = uriBuilder.path("/vehicles/{customerId}").buildAndExpand(vehicle.getVehicleId())
        .toUri();
    GlobalResponse body = new GlobalResponse("Vehicle created");
    return ResponseEntity.created(location).body(body);
  }

  @DeleteMapping("customers/{customerId}")
  public ResponseEntity<GlobalResponse> deleteCustomer(
      @PathVariable("customerId") int customerId) {
    customerService.deleteCustomerByCustomerId(customerId);
    GlobalResponse body = new GlobalResponse("Customer deleted");
    return ResponseEntity.ok(body);
  }

  @DeleteMapping("vehicles/{vehicleId}")
  public ResponseEntity<GlobalResponse> deleteVehicle(
      @PathVariable("vehicleId") int vehicleId) {
    customerService.deleteVehicleByVehicleId(vehicleId);
    GlobalResponse body = new GlobalResponse("Vehicle deleted");
    return ResponseEntity.ok(body);
  }

  @PutMapping("customers/{customerId}")
  public ResponseEntity<GlobalResponse> updateCustomer(
      @PathVariable("customerId") int customerId,
      @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest) {
    customerService.updateCustomerByCustomerId(customerId, customerUpdateRequest);
    GlobalResponse body = new GlobalResponse("Customer updated");
    return ResponseEntity.ok(body);
  }

  @PutMapping("addresses/{addressId}")
  public ResponseEntity<GlobalResponse> updateCustomerAddress(
      @PathVariable("addressId") int addressId,
      @RequestBody @Valid CustomerAddressUpdateRequest customerAddressUpdateRequest) {
    customerService.updateCustomerAddressByCustomerId(addressId, customerAddressUpdateRequest);
    GlobalResponse body = new GlobalResponse("CustomerAddress updated");
    return ResponseEntity.ok(body);
  }

  @PutMapping("vehicles/{vehicleId}")
  public ResponseEntity<GlobalResponse> updateVehicle(
      @PathVariable("vehicleId") int vehicleId,
      @RequestBody @Valid VehicleUpdateRequest vehicleUpdateRequest) {
    customerService.updateVehicleByVehicleId(vehicleId, vehicleUpdateRequest);
    GlobalResponse body = new GlobalResponse("Vehicle updated");
    return ResponseEntity.ok(body);
  }
}
