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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customer", description = "顧客情報に関するAPI")
public class CustomerController {

  private final CustomerService customerService;


  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * 顧客情報を取得する処理
   *
   * @param name
   * @param kana
   * @param email
   * @return 顧客情報のリスト
   */
  @Operation(summary = "顧客情報を取得する処理"
      , description = "顧客名、顧客名カナ、メールアドレスを指定して顧客情報を取得します。"
  )
  @Parameters({
      @Parameter(name = "name", description = "顧客名(部分一致検索)"),
      @Parameter(name = "kana", description = "顧客名カナ(部分一致検索)"),
      @Parameter(name = "email", description = "メールアドレス(完全一致検索)")
  })
  @GetMapping("/customers")
  public List<CustomerInformationRequest> findCustomersByRequestParam(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "kana", required = false) String kana,
      @RequestParam(value = "email", required = false) String email) {
    List<CustomerInformationRequest> result = customerService.findCustomerInformation(name, kana,
        email);
    return result;
  }

  /**
   * @param plateVehicleNumber
   * @return 顧客情報のリスト
   */
  @Operation(summary = "顧客情報を取得する処理",
      description = "車両ナンバー４桁を指定して顧客情報を取得します。")
  @Parameter(name = "plateVehicleNumber", description = "車両ナンバー４桁")
  @GetMapping("/vehicles/numbers/{plateVehicleNumber}")
  public List<CustomerInformationRequest> findCustomersByPlateVehicleNumber(
      @PathVariable String plateVehicleNumber) {
    List<CustomerInformationRequest> result = customerService.findInformationByPlateVehicleNumber(
        plateVehicleNumber);
    return result;
  }

  /**
   * 電話番号を指定して顧客情報を取得する処理
   *
   * @param phoneNumber 電話番号(XXX-XXXX-XXXX形式)
   * @return 顧客情報のリスト
   */
  @Operation(summary = "顧客情報を取得する処理",
      description = "電話番号を指定して顧客情報を取得します。(完全一致検索)")
  @Parameter(name = "phoneNumber", description = "電話番号(XXX-XXXX-XXXX形式)")

  @GetMapping("customers/phone/{phoneNumber}")
  public List<CustomerInformationRequest> searchUsersByPhoneNumber(
      @PathVariable String phoneNumber) {
    List<CustomerInformationRequest> result = customerService.findCustomerInformationByPhoneNumber(
        phoneNumber);
    return result;
  }

  /**
   * 顧客情報を登録する処理。顧客IDは自動採番されます。
   *
   * @param customerCreateRequest 顧客情報のリクエストボディ
   * @return 登録結果のレスポンス
   */
  @Operation(summary = "顧客情報を登録する処理",
      description = "新規顧客情報を登録します。customerIdは自動採番されます。")
  @Parameter(name = "customerCreateRequest", description = "顧客情報のリクエストボディ")

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

  /**
   * 顧客住所を登録する処理。顧客IDを指定して住所情報を登録します。
   *
   * @param customerId                   顧客ID
   * @param customerAddressCreateRequest 住所情報のリクエストボディ
   * @return 登録結果のレスポンス
   */
  @Operation(summary = "顧客住所を登録する処理",
      description = "customerIdを指定して住所情報を登録します。")
  @Parameter(name = "customerId", description = "顧客ID")

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

  /**
   * 車両情報を登録する処理。顧客IDを指定して車両情報を登録します。
   *
   * @param customerId           顧客ID
   * @param vehicleCreateRequest 車両情報のリクエストボディ
   * @return 登録結果のレスポンス
   */
  @Operation(summary = "車両情報を登録する処理",
      description = "customerIdを指定して車両情報を登録します。")
  @Parameter(name = "customerId", description = "顧客ID")
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

  /**
   * 顧客情報をIDで取得する処理
   *
   * @param customerId 顧客ID
   * @return 顧客情報
   */
  @Operation(summary = "顧客情報をIDで削除する処理",
      description = "customerIdを指定して顧客情報を削除します。"
          + "それに伴い、関連する住所情報と車両情報も削除されます。")
  @Parameter(name = "customerId", description = "顧客ID")
  @DeleteMapping("customers/{customerId}")
  public ResponseEntity<GlobalResponse> deleteCustomer(
      @PathVariable("customerId") int customerId) {
    customerService.deleteCustomerByCustomerId(customerId);
    GlobalResponse body = new GlobalResponse("Customer deleted");
    return ResponseEntity.ok(body);
  }

  /**
   * 車両情報をIDで削除する処理。車両情報はなくならず、非アクティブ状態になります。
   *
   * @param vehicleId 車両ID
   * @return 削除結果のレスポンス
   */
  @Operation(summary = "車両情報をIDで削除する処理",
      description = "vehicleIdを指定して車両情報を削除します。"
          + "車両情報はなくならず、非アクティブ状態になります。")
  @Parameter(name = "vehicleId", description = "車両ID")
  @DeleteMapping("vehicles/{vehicleId}")
  public ResponseEntity<GlobalResponse> deleteVehicle(
      @PathVariable("vehicleId") int vehicleId) {
    customerService.deleteVehicleByVehicleId(vehicleId);
    GlobalResponse body = new GlobalResponse("Vehicle deleted");
    return ResponseEntity.ok(body);
  }

  /**
   * 顧客情報をIDで更新する処理
   *
   * @param customerId            顧客ID
   * @param customerUpdateRequest 顧客情報の更新リクエストボディ
   * @return 更新結果のレスポンス
   */
  @Operation(summary = "顧客情報をIDで更新する処理",
      description = "customerIdを指定して顧客情報を更新します。")
  @Parameter(name = "customerId", description = "顧客ID")
  @PutMapping("customers/{customerId}")
  public ResponseEntity<GlobalResponse> updateCustomer(
      @PathVariable("customerId") int customerId,
      @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest) {
    customerService.updateCustomerByCustomerId(customerId, customerUpdateRequest);
    GlobalResponse body = new GlobalResponse("Customer updated");
    return ResponseEntity.ok(body);
  }

  /**
   * 顧客住所をIDで更新する処理
   *
   * @param addressId                    顧客住所ID
   * @param customerAddressUpdateRequest 顧客住所の更新リクエストボディ
   * @return 更新結果のレスポンス
   */
  @Operation(summary = "顧客住所をIDで更新する処理",
      description = "addressIdを指定して顧客住所を更新します。")
  @Parameter(name = "addressId", description = "顧客住所ID")
  @PutMapping("addresses/{addressId}")
  public ResponseEntity<GlobalResponse> updateCustomerAddress(
      @PathVariable("addressId") int addressId,
      @RequestBody @Valid CustomerAddressUpdateRequest customerAddressUpdateRequest) {
    customerService.updateCustomerAddressByCustomerId(addressId, customerAddressUpdateRequest);
    GlobalResponse body = new GlobalResponse("CustomerAddress updated");
    return ResponseEntity.ok(body);
  }

  /**
   * 車両情報をIDで更新する処理
   *
   * @param vehicleId            車両ID
   * @param vehicleUpdateRequest 車両情報の更新リクエストボディ
   * @return 更新結果のレスポンス
   */
  @Operation(summary = "車両情報をIDで更新する処理",
      description = "vehicleIdを指定して車両情報を更新します。")
  @Parameter(name = "vehicleId", description = "車両ID")
  @PutMapping("vehicles/{vehicleId}")
  public ResponseEntity<GlobalResponse> updateVehicle(
      @PathVariable("vehicleId") int vehicleId,
      @RequestBody @Valid VehicleUpdateRequest vehicleUpdateRequest) {
    customerService.updateVehicleByVehicleId(vehicleId, vehicleUpdateRequest);
    GlobalResponse body = new GlobalResponse("Vehicle updated");
    return ResponseEntity.ok(body);
  }
}
