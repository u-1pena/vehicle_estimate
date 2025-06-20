package com.u1pena.estimateapi.customer.service;

import com.u1pena.estimateapi.customer.converter.CustomerAddressCreateConverter;
import com.u1pena.estimateapi.customer.converter.CustomerAddressUpdateConverter;
import com.u1pena.estimateapi.customer.converter.CustomerCreateConverter;
import com.u1pena.estimateapi.customer.converter.CustomerInformationConverter;
import com.u1pena.estimateapi.customer.converter.CustomerUpdateConverter;
import com.u1pena.estimateapi.customer.converter.VehicleCreateConverter;
import com.u1pena.estimateapi.customer.converter.VehicleUpdateConverter;
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
import com.u1pena.estimateapi.customer.exception.CustomerAddressException.CustomerAddressAlreadyException;
import com.u1pena.estimateapi.customer.exception.CustomerAddressException.CustomerAddressNotFoundException;
import com.u1pena.estimateapi.customer.exception.CustomerException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsEmailException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsPhoneNumberException;
import com.u1pena.estimateapi.customer.exception.CustomerException.InvalidSearchParameterException;
import com.u1pena.estimateapi.customer.exception.VehicleException.AlreadyExistsVehicleException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleInactiveException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleNotFoundException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleYearInvalidException;
import com.u1pena.estimateapi.customer.mapper.CustomerRepository;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  /*@RequestParamがname,kana,emailの場合
   * 条件検索へ移行するメソッド*/
  public List<CustomerInformationRequest> findCustomerInformation(String name,
      String kana, String email) {
    String pramName = defineSearchCriteria(name, kana,
        email);//英語弱者のメモ：defineSearchCriteria(検索条件を定義）

    //switch文でパラメーターの可読性アップ！確かに見やすいけど、if文のほうがコードが短くなるので、どちらがいいかは微妙かも・・
    return switch (pramName) {
      case "name" -> findCustomerInformationByName(name);
      case "kana" -> findCustomerInformationByNameKana(kana);
      case "email" -> findCustomerInformationByEmail(email);
      //例外をスローする
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

  private List<CustomerInformationRequest> findCustomerInformationByName(String name) {
    List<Customer> customers = findCustomerByName(name);
    return CustomerInformationConverter.convertToCustomerInformationDtoList(
        customers,
        this::findCustomerAddressListByCustomerId,
        this::findVehicleByCustomerId
    );
  }

  private List<Customer> findCustomerByName(String name) {
    List<Customer> customer = customerRepository.findCustomerByName(name);
    return customer;
  }

  private List<CustomerInformationRequest> findCustomerInformationByNameKana(String kana) {
    List<Customer> customers = findByNameKana(kana);
    return CustomerInformationConverter.convertToCustomerInformationDtoList(
        customers,
        this::findCustomerAddressListByCustomerId,
        this::findVehicleByCustomerId
    );
  }

  private List<Customer> findByNameKana(String kana) {
    List<Customer> Customers = customerRepository.findCustomerByNameKana(kana);
    return Customers;
  }

  private List<CustomerInformationRequest> findCustomerInformationByEmail(String email) {
    Optional<Customer> customer = findCustomerByEmail(email);
    List<Customer> customers = customer.map(Collections::singletonList)
        .orElse(Collections.emptyList());
    return CustomerInformationConverter.convertToCustomerInformationDtoList(
        customers,
        this::findCustomerAddressListByCustomerId,
        this::findVehicleByCustomerId
    );
  }

  private Optional<Customer> findCustomerByEmail(String email) {
    Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
    return customer;
  }

  private CustomerInformationRequest convertToCustomerInformationDtoFromCustomer(
      Customer customer) {
    CustomerAddress customerAddress = findCustomerAddressListByCustomerId(customer.getCustomerId());
    List<Vehicle> vehicles = findVehicleByCustomerId(customer.getCustomerId());
    return CustomerInformationConverter.convertToCustomerInformationDto(customer, customerAddress,
        vehicles);
  }


  //車両番号４桁で検索するメソッド
  public List<CustomerInformationRequest> findInformationByPlateVehicleNumber(
      String plateVehicleNumber) {
    //ここで車両番号を検索するメソッドを呼び出す
    List<Vehicle> vehicles = findCustomerByPlateVehicleNumber(plateVehicleNumber);
    //車両が見つからない場合は空のリストを返す
    if (vehicles.isEmpty()) {
      return Collections.emptyList();
    }
    //顧客ごとに `Map<customerId, CustomerInformationRequest>` を作る
    Map<Integer, CustomerInformationRequest> customerInfoMap = new HashMap<>();

    for (Vehicle vehicle : vehicles) {
      int customerId = vehicle.getCustomerId();

      // すでに `CustomerInformationRequest` が作成されているか確認
      if (!customerInfoMap.containsKey(customerId)) {
        Customer customer = findCustomerByCustomerId(customerId);
        CustomerAddress customerAddress = findCustomerAddressListByCustomerId(customerId);

        //初回ならCustomerInformationDtoを作成し、マップに追加
        CustomerInformationRequest dto = CustomerInformationConverter.convertToCustomerInformationDto(
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
    List<Vehicle> vehicles = customerRepository.findVehicleByPlateNumber(
        plateVehicleNumber);
    return vehicles;
  }

  public List<CustomerInformationRequest> findCustomerInformationById(int customerId) {
    Customer customer = findCustomerByCustomerId(customerId);
    CustomerAddress customerAddress = findCustomerAddressListByCustomerId(customerId);
    List<Vehicle> vehicles = findVehicleByCustomerId(customerId);
    return List.of(
        CustomerInformationConverter.convertToCustomerInformationDto(customer, customerAddress,
            vehicles));
  }

  private CustomerAddress findCustomerAddressListByCustomerId(int customerId) {
    return customerRepository.findCustomerAddressByCustomerId(customerId)
        .orElse(new CustomerAddress());
  }

  private CustomerAddress validateCustomerAddressExistsByCustomerId(int customerId) {
    return customerRepository.findCustomerAddressByCustomerId(customerId)
        .orElseThrow(() -> new CustomerAddressNotFoundException(
            "Not registered for customer address ID:" + customerId));
  }

  private List<Vehicle> findVehicleByCustomerId(int id) {
    return customerRepository.findVehicleByCustomerId(id)
        .stream()
        .filter(vehicle -> vehicle.getCustomerId() == id)
        .collect(Collectors.toList());
  }

  public List<CustomerInformationRequest> findCustomerInformationByPhoneNumber(String phoneNumber) {
    Optional<Customer> customer = findCustomerByPhoneNumber(phoneNumber);
    List<Customer> customers = customer.map(Collections::singletonList)
        .orElse(Collections.emptyList());
    return CustomerInformationConverter.convertToCustomerInformationDtoList(
        customers,
        this::findCustomerAddressListByCustomerId,
        this::findVehicleByCustomerId
    );
  }

  private Optional<Customer> findCustomerByPhoneNumber(String phoneNumber) {
    Optional<Customer> customer = customerRepository.findCustomerByPhoneNumber(phoneNumber);
    return customer;
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


  public CustomerAddress registerCustomerAddress(int customerId,
      CustomerAddressCreateRequest customerAddressCreateRequest) {
    Customer customer = findCustomerByCustomerId(customerId);
    checkAlreadyExistCustomerAddress(customerId);
    CustomerAddress customerAddress = CustomerAddressCreateConverter.customerAddressConvertToEntity(
        customer, customerAddressCreateRequest);
    createCustomerAddress(customerAddress);
    return customerAddress;
  }

  private void checkAlreadyExistCustomerAddress(int id) {
    customerRepository.findCustomerAddressByCustomerId(id)
        .ifPresent(userDetail -> {
          throw new CustomerAddressAlreadyException(
              "The customer with this ID has already completed address registration: " + id);
        });
  }


  public Vehicle registerVehicle(int customerId, VehicleCreateRequest vehicleCreateRequest) {
    Customer customer = findCustomerByCustomerId(customerId);
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
    customerRepository.findVehicleByLicensePlateExactMatch(vehicle.getPlateRegion(),
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

  public void deleteCustomerByCustomerId(int customerId) {
    findCustomerByCustomerId(customerId);
    deleteCustomer(customerId);
  }

  public void deleteVehicleByVehicleId(int vehicleId) {
    Vehicle vehicle = findVehicleByVehicleId(vehicleId);
    if (!vehicle.isActive()) {
      throw new VehicleInactiveException("Vehicle is already inactive.");
    }
    customerRepository.deactivateVehicle(vehicle.getVehicleId());
  }

  public void updateCustomerByCustomerId(int customerId,
      CustomerUpdateRequest customerUpdateRequest) {
    Customer customer = findCustomerByCustomerId(customerId);
    CustomerUpdateConverter.customerUpdateConvertToEntity(customer, customerUpdateRequest);
    duplicateCheckForUpdates(customer.getPhoneNumber(), customer.getEmail(), customerId);
    updateCustomer(customer);
  }

  private void duplicateCheckForUpdates(String phoneNumber, String email, int customerId) {
    updateCheckAlreadyExistPhoneNumber(phoneNumber, customerId);
    updateCheckAlreadyExistEmail(email, customerId);
  }

  private void updateCheckAlreadyExistPhoneNumber(String phoneNumber, int customerId) {
    customerRepository.findCustomerByPhoneNumber(phoneNumber)
        .ifPresent(customer -> {
          if (customer.getCustomerId() != customerId) {
            throw new AlreadyExistsPhoneNumberException();
          }
        });
  }

  private void updateCheckAlreadyExistEmail(String email, int customerId) {
    customerRepository.findCustomerByEmail(email)
        .ifPresent(customer -> {
          if (customer.getCustomerId() != customerId) {
            throw new AlreadyExistsEmailException();
          }
        });
  }

  public void updateCustomerAddressByCustomerId(int customerId,
      CustomerAddressUpdateRequest customerAddressUpdateRequest) {
    Customer customer = findCustomerByCustomerId(customerId);
    CustomerAddress customerAddress = validateCustomerAddressExistsByCustomerId(customerId);
    CustomerAddressUpdateConverter.customerAddressUpdateConvertToEntity(customer,
        customerAddress, customerAddressUpdateRequest);
    updateCustomerAddress(customerAddress);
  }

  public void updateVehicleByVehicleId(int vehicleId,
      VehicleUpdateRequest vehicleUpdateRequest) {
    Vehicle vehicle = findVehicleByVehicleId(vehicleId);
    VehicleUpdateConverter.vehicleUpdateConvertToEntity(vehicle, vehicleUpdateRequest);
    checkVehiclesYearValid(String.valueOf(vehicle.getYear()));
    updateCheckAlreadyExistVehicleByPlateNumber(vehicle);
    updateVehicle(vehicle);
  }

  /*
  更新時のナンバープレートの重複チェック
  自身のナンバーを除外
   */
  private void updateCheckAlreadyExistVehicleByPlateNumber(Vehicle vehicle) {
    customerRepository.findVehicleByLicensePlateExactMatch(vehicle.getPlateRegion(),
            vehicle.getPlateCategoryNumber(), vehicle.getPlateHiragana(),
            vehicle.getPlateVehicleNumber())
        .ifPresent(vehicle1 -> {
          if (vehicle1.getVehicleId() != vehicle.getVehicleId()) {
            throw new AlreadyExistsVehicleException();
          }
        });
  }

  private Vehicle findVehicleByVehicleId(int vehicleId) {
    return customerRepository.findVehicleByVehicleId(vehicleId)
        .orElseThrow(
            () -> new VehicleNotFoundException("Not registered for vehicle ID:" + vehicleId));
  }

  private void updateCustomer(Customer customer) {
    customerRepository.updateCustomer(customer);
  }

  private void updateCustomerAddress(CustomerAddress customerAddress) {
    customerRepository.updateCustomerAddress(customerAddress);
  }

  private void updateVehicle(Vehicle vehicle) {
    customerRepository.updateVehicle(vehicle);
  }

  private Customer findCustomerByCustomerId(int customerId) {
    return customerRepository.findCustomerByCustomerId(customerId)
        .orElseThrow(
            () -> new CustomerException.CustomerNotFoundException(
                "Not registered for customer ID:" + customerId));
  }

  private void deleteCustomer(int customerId) {
    customerRepository.deleteCustomer(customerId);
  }
}
