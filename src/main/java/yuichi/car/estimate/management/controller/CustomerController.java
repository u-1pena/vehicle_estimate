package yuichi.car.estimate.management.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import yuichi.car.estimate.management.controller.Response.CreateResponse;
import yuichi.car.estimate.management.dto.CustomerInformationDto;
import yuichi.car.estimate.management.dto.request.CustomerAddressCreateRequest;
import yuichi.car.estimate.management.dto.request.CustomerCreateRequest;
import yuichi.car.estimate.management.dto.request.VehicleCreateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.service.CustomerService;

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
  public List<CustomerInformationDto> findCustomersByRequestParam(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "kana", required = false) String kana,
      @RequestParam(value = "email", required = false) String email) {
    List<CustomerInformationDto> result = customerService.findCustomerInformation(name, kana,
        email);
    return result;
  }

  /*
  車両ナンバー４桁から顧客情報を検索する処理
   */
  @GetMapping("/vehicles/numbers/{plateVehicleNumber}")
  public List<CustomerInformationDto> findCustomersByPlateVehicleNumber(
      @PathVariable String plateVehicleNumber) {
    List<CustomerInformationDto> result = customerService.findInformationByPlateVehicleNumber(
        plateVehicleNumber);
    return result;
  }

  /*
  電話番号から顧客情報を検索する処理
  0x0-xxxx-xxxxの形式で検索を行う
   */
  @GetMapping("customers/phone/{phoneNumber}")
  public List<CustomerInformationDto> searchUsersByPhoneNumber(
      @PathVariable String phoneNumber) {
    List<CustomerInformationDto> result = customerService.findCustomerInformationByPhoneNumber(
        phoneNumber);
    return result;
  }

  /*
  顧客情報を登録する処理
   */
  @PostMapping("/customers")
  public ResponseEntity<CreateResponse> createCustomer(
      @RequestBody @Valid CustomerCreateRequest customerCreateRequest,
      UriComponentsBuilder uriBuilder) {
    Customer customer = customerService.registerCustomer(customerCreateRequest);
    URI location = uriBuilder.path("/users/{addressId}").buildAndExpand(customer.getCustomerId())
        .toUri();
    CreateResponse body = new CreateResponse("Customer created");
    return ResponseEntity.created(location).body(body);
  }

  /*
  住所情報を登録する処理
   */
  @PostMapping("addresses/{addressId}")
  public ResponseEntity<CreateResponse> createUserDetail(
      @PathVariable("addressId") int addressId,
      @RequestBody @Valid CustomerAddressCreateRequest customerAddressCreateRequest, // 専用リクエストクラス
      UriComponentsBuilder uriBuilder) {
    CustomerAddress customerAddress = customerService.registerCustomerAddress(addressId,
        customerAddressCreateRequest);
    URI location = uriBuilder.path("/address/{addressId}")
        .buildAndExpand(customerAddress.getAddressId()).toUri();
    CreateResponse body = new CreateResponse("CustomerAddress Created");
    return ResponseEntity.created(location).body(body);
  }

  /*
  車両情報を登録する処理
   */
  @PostMapping("vehicles/{customerId}")
  public ResponseEntity<CreateResponse> createVehicle(
      @PathVariable("customerId") int customerId,
      @RequestBody @Valid VehicleCreateRequest vehicleCreateRequest,
      UriComponentsBuilder uriBuilder) {
    Vehicle vehicle = customerService.registerVehicle(customerId, vehicleCreateRequest);
    URI location = uriBuilder.path("/vehicles/{customerId}").buildAndExpand(vehicle.getVehicleId())
        .toUri();
    CreateResponse body = new CreateResponse("Vehicle created");
    return ResponseEntity.created(location).body(body);
  }
}
