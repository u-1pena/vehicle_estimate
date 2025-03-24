package yuichi.car.estimate.management.service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import yuichi.car.estimate.management.controller.exception.CustomerAddressException.CustomerAddressAlreadyException;
import yuichi.car.estimate.management.controller.exception.CustomerException.AlreadyExistsEmailException;
import yuichi.car.estimate.management.controller.exception.CustomerException.AlreadyExistsPhoneNumberException;
import yuichi.car.estimate.management.controller.exception.CustomerException.CustomerNotFoundException;
import yuichi.car.estimate.management.controller.exception.CustomerException.InvalidSearchParameterException;
import yuichi.car.estimate.management.controller.exception.VehicleException.AlreadyExistsVehicleException;
import yuichi.car.estimate.management.controller.exception.VehicleException.VehicleYearInvalidException;
import yuichi.car.estimate.management.converter.CustomerAddressCreateConverter;
import yuichi.car.estimate.management.converter.CustomerCreateConverter;
import yuichi.car.estimate.management.converter.CustomerInformationConverter;
import yuichi.car.estimate.management.converter.VehicleCreateConverter;
import yuichi.car.estimate.management.dto.CustomerInformationDto;
import yuichi.car.estimate.management.dto.request.CustomerAddressCreateRequest;
import yuichi.car.estimate.management.dto.request.CustomerCreateRequest;
import yuichi.car.estimate.management.dto.request.VehicleCreateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.mapper.CustomerRepository;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  /*@RequestParamがname,kana,emailの場合
   * 条件検索へ移行するメソッド*/
  public List<CustomerInformationDto> findCustomerInformation(String name,
      String kana, String email) {
    String pramName = defineSearchCriteria(name, kana,
        email);//英語弱者のメモ：defineSearchCriteria(検索条件を定義）

    //switch文でパラメーターの可読性アップ！確かに見やすいけど、if文のほうがコードが短くなるので、どちらがいいかは微妙かも・・
    return switch (pramName) {
      case "name" -> findCustomerInformationByName(name);
      case "kana" -> findCustomerInformationByNameKana(kana);
      case "email" -> findCustomerInformationByEmail(email);
      default -> throw new InvalidSearchParameterException();
    };
  }

  /*検索条件を定義するメソッド
   * name,kana,emailのどれかで検索条件を定義する
   * それ以外の場合はdefaultを返す（defaultはエラーを返す）
   */
  private String defineSearchCriteria(String name, String kana, String email
  ) {
    if (name != null && !name.isBlank()) {
      return "name";
    }
    if (kana != null && !kana.isBlank()) {
      return "kana";
    }
    if (email != null && !email.isBlank()) {
      return "email";
    }
    return "default";
  }

  //車両番号４桁で検索するメソッド
  public List<CustomerInformationDto> findInformationByPlateVehicleNumber(
      String plateVehicleNumber) {
    //ここで車両番号を検索するメソッドを呼び出す
    List<Vehicle> vehicles = findCustomerByPlateVehicleNumber(plateVehicleNumber);
    //車両が見つからない場合は空のリストを返す
    if (vehicles.isEmpty()) {
      return Collections.emptyList();
    }
    //顧客ごとに `Map<customerId, CustomerInformationDto>` を作る
    Map<Integer, CustomerInformationDto> customerInfoMap = new HashMap<>();

    for (Vehicle vehicle : vehicles) {
      int customerId = vehicle.getCustomerId();

      // すでに `CustomerInformationDto` が作成されているか確認
      if (!customerInfoMap.containsKey(customerId)) {
        Customer customer = findCustomerById(customerId);
        CustomerAddress customerAddress = findCustomerAddressById(customerId);

        //初回ならCustomerInformationDtoを作成し、マップに追加
        CustomerInformationDto dto = CustomerInformationConverter.convertToCustomerInformationDto(
            customer, customerAddress, new ArrayList<>() // 初期の `vehicles` を空リストに
        );
        customerInfoMap.put(customerId, dto);
      }
      //vehiclesに現在の車両を追加
      customerInfoMap.get(customerId).getVehicles().add(vehicle);
    }
    //最終的なリストに変換
    return new ArrayList<>(customerInfoMap.values());
  }

  private List<Vehicle> findCustomerByPlateVehicleNumber(String plateVehicleNumber) {
    List<Vehicle> vehicles = customerRepository.findCustomerByPlateVehicleNumber(
        plateVehicleNumber);
    return vehicles;
  }

  public List<CustomerInformationDto> findCustomerInformationById(int id) {
    Customer customer = findCustomerById(id);
    CustomerAddress customerAddress = findCustomerAddressById(id);
    List<Vehicle> vehicles = findVehicleByCustomerId(id);
    return List.of(CustomerInformationConverter.convertToCustomerInformationDto(customer,
        customerAddress, vehicles));
  }

  private Customer findCustomerById(int id) {
    return customerRepository.findCustomerById(id)
        .orElseThrow(
            () -> new CustomerNotFoundException("Not registered for customer ID:" + id));
  }

  private CustomerAddress findCustomerAddressById(int id) {
    return customerRepository.findCustomerAddressById(id)
        .orElse(new CustomerAddress());
  }

  private List<Vehicle> findVehicleByCustomerId(int id) {
    return customerRepository.findVehicleByCustomerId(id)
        .stream()
        .filter(vehicle -> vehicle.getCustomerId() == id)
        .collect(Collectors.toList());
  }


  private List<CustomerInformationDto> findCustomerByCriteria(List<Customer> customers) {
    return customers.stream()
        .map(this::convertToCustomerInformationDtoFromCustomer)
        .collect(Collectors.toList());
  }

  private CustomerInformationDto convertToCustomerInformationDtoFromCustomer(Customer customer) {
    CustomerAddress customerAddress = findCustomerAddressById(customer.getCustomerId());
    List<Vehicle> vehicles = findVehicleByCustomerId(customer.getCustomerId());
    return CustomerInformationConverter.convertToCustomerInformationDto(customer, customerAddress,
        vehicles);
  }

  private List<CustomerInformationDto> findCustomerInformationByName(String name) {
    List<Customer> customers = findCustomerByName(name);
    return findCustomerByCriteria(customers);
  }

  private List<Customer> findCustomerByName(String name) {
    List<Customer> customer = customerRepository.findCustomerByName(name);
    return customer;
  }

  private List<CustomerInformationDto> findCustomerInformationByNameKana(String kana) {
    List<Customer> customers = findByNameKana(kana);
    return findCustomerByCriteria(customers);
  }

  //ユーザー情報を取得する処理 完全一致のEmailで検索しユーザー情報を照会します
  private List<CustomerInformationDto> findCustomerInformationByEmail(String email) {
    Optional<Customer> customer = findCustomerByEmail(email);
    List<Customer> customers = customer.map(Collections::singletonList)
        .orElse(Collections.emptyList());
    return findCustomerByCriteria(customers);
  }

  private Optional<Customer> findCustomerByEmail(String email) {
    Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
    return customer;
  }

  public List<CustomerInformationDto> findCustomerInformationByPhoneNumber(String phoneNumber) {
    Optional<Customer> customer = findCustomerByPhoneNumber(phoneNumber);
    List<Customer> customers = customer.map(Collections::singletonList)
        .orElse(Collections.emptyList());
    return findCustomerByCriteria(customers);
  }

  private Optional<Customer> findCustomerByPhoneNumber(String phoneNumber) {
    Optional<Customer> customer = customerRepository.findCustomerByPhoneNumber(phoneNumber);
    return customer;
  }

  private List<Customer> findByNameKana(String kana) {
    List<Customer> Customers = customerRepository.findCustomerByNameKana(kana);
    return Customers;
  }

  public Customer registerCustomer(CustomerCreateRequest customerCreateRequest) {
    checkAlreadyExistEmail(customerCreateRequest.getEmail());
    checkAlreadyExistPhoneNumber(customerCreateRequest.getPhoneNumber());
    Customer customer = CustomerCreateConverter.customerConvertToEntity(customerCreateRequest);
    createCustomer(customer);
    return customer;
  }

  private void checkAlreadyExistPhoneNumber(String phoneNumber) {
    customerRepository.findCustomerByPhoneNumber(phoneNumber)
        .ifPresent(customer -> {
          throw new AlreadyExistsPhoneNumberException();
        });
  }

  private void checkAlreadyExistEmail(String email) {
    customerRepository.findCustomerByEmail(email)
        .ifPresent(user -> {
          throw new AlreadyExistsEmailException();
        });
  }

  private void createCustomer(Customer customer) {
    customerRepository.createCustomer(customer);
  }


  public CustomerAddress registerCustomerAddress(int addressId,
      CustomerAddressCreateRequest customerAddressCreateRequest) {
    Customer customer = findCustomerById(addressId);
    checkAlreadyExistCustomerAddress(addressId);
    CustomerAddress customerAddress = CustomerAddressCreateConverter.customerAddressConvertToEntity(
        customer, customerAddressCreateRequest);
    createCustomerAddress(customerAddress);
    return customerAddress;
  }

  private void checkAlreadyExistCustomerAddress(int id) {
    customerRepository.findCustomerAddressById(id)
        .map(userDetail -> {
          throw new CustomerAddressAlreadyException(
              "The customer with this ID has already completed address registration: " + id);
        });
  }


  public Vehicle registerVehicle(int customerId, VehicleCreateRequest vehicleCreateRequest) {
    Customer customer = findCustomerById(customerId);
    Vehicle vehicle = VehicleCreateConverter.vehicleConvertToEntity(customer, vehicleCreateRequest);
    checkVehiclesYearValid(String.valueOf(vehicle.getYear()));
    checkAlreadyExistVehicleByPlateNumber(vehicle);
    createVehicle(vehicle);
    return vehicle;
  }

  private void checkVehiclesYearValid(String year) {
    YearMonth vehiclesYear = YearMonth.parse(year);
    YearMonth currentDay = YearMonth.now();
    if (vehiclesYear.isAfter(currentDay)) {
      throw new VehicleYearInvalidException();
    }
  }

  private void checkAlreadyExistVehicleByPlateNumber(Vehicle vehicle) {
    customerRepository.findCustomerByLicensePlateExactMatch(vehicle.getPlateRegion(),
            vehicle.getPlateCategoryNumber(), vehicle.getPlateHiragana(),
            vehicle.getPlateVehicleNumber())
        .ifPresent(vehicle1 -> {
          throw new AlreadyExistsVehicleException();
        });
  }

  private void createCustomerAddress(CustomerAddress customerAddress) {
    customerRepository.createCustomerAddress(customerAddress);
  }

  private void createVehicle(Vehicle vehicle) {
    customerRepository.createVehicle(vehicle);
  }
}
