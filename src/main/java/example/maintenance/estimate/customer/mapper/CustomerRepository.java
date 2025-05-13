package example.maintenance.estimate.customer.mapper;

import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.Vehicle;
import example.maintenance.estimate.customer.entity.enums.PlateRegion;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerRepository {

  Optional<Customer> findCustomerByCustomerId(int customerId);

  Optional<CustomerAddress> findCustomerAddressByCustomerId(int customerId);

  List<Vehicle> findVehicleByCustomerId(int customerId);

  List<Customer> findCustomerByName(String name);

  List<Customer> findCustomerByNameKana(String kana);

  Optional<Customer> findCustomerByEmail(String email);

  List<Vehicle> findVehicleByPlateNumber(String plateNumber);

  Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);

  Optional<Vehicle> findVehicleByLicensePlateExactMatch(PlateRegion plateRegion,
      String plateCategoryNumber, String plateHiragana, String plateVehicleNumber);

  Optional<Vehicle> findVehicleByVehicleId(int vehicleId);

  void createCustomer(Customer customer);

  void createCustomerAddress(CustomerAddress customerAddress);

  void createVehicle(Vehicle vehicle);//理論削除

  void deleteCustomer(int customerId);

  void deactivateVehicle(int vehicleId);

  void updateCustomer(Customer customer);

  void updateCustomerAddress(CustomerAddress customerAddress);

  void updateVehicle(Vehicle vehicle);

  //@test用
  List<Customer> findAllCustomers();

  List<CustomerAddress> findAllCustomerAddresses();

  List<Vehicle> findAllVehicles();
}
