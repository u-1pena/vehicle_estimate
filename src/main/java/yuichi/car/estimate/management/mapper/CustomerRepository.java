package yuichi.car.estimate.management.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.CustomerAddress;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.entity.enums.PlateRegion;

@Mapper
public interface CustomerRepository {

  Optional<Customer> findCustomerByCustomerId(int customerId);

  Optional<CustomerAddress> findCustomerAddressByCustomerId(int customerId);

  List<Vehicle> findVehicleByCustomerId(int customerId);

  List<Customer> findCustomerByName(String name);

  List<Customer> findCustomerByNameKana(String kana);

  Optional<Customer> findCustomerByEmail(String email);

  List<Vehicle> findCustomerByPlateVehicleNumber(String plateVehicleNumber);

  Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);

  Optional<Vehicle> findCustomerByLicensePlateExactMatch(PlateRegion plateRegion,
      String plateCategoryNumber, String plateHiragana, String plateVehicleNumber);

  Optional<Vehicle> findVehicleByVehicleId(int vehicleId);

  void createCustomer(Customer customer);

  void createCustomerAddress(CustomerAddress customerAddress);

  void createVehicle(Vehicle vehicle);

  void deleteCustomer(int customerId);

  void deleteVehicle(int vehicleId);

  void updateCustomer(Customer customer);

  void updateCustomerAddress(CustomerAddress customerAddress);

  void updateVehicle(Vehicle vehicle);

  // 以下のメソッドは、テスト用に追加
  List<Customer> findAllCustomers();

  List<CustomerAddress> findAllCustomerAddresses();

  List<Vehicle> findAllVehicles();
}
