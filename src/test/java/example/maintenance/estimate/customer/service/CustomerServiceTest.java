package example.maintenance.estimate.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import example.maintenance.estimate.customer.controller.exception.CustomerAddressException.CustomerAddressAlreadyException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.AlreadyExistsEmailException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.AlreadyExistsPhoneNumberException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.CustomerNotFoundException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.InvalidSearchParameterException;
import example.maintenance.estimate.customer.controller.exception.VehicleException;
import example.maintenance.estimate.customer.controller.exception.VehicleException.VehicleYearInvalidException;
import example.maintenance.estimate.customer.dto.CustomerInformationDto;
import example.maintenance.estimate.customer.dto.request.CustomerAddressCreateRequest;
import example.maintenance.estimate.customer.dto.request.CustomerAddressUpdateRequest;
import example.maintenance.estimate.customer.dto.request.CustomerCreateRequest;
import example.maintenance.estimate.customer.dto.request.CustomerUpdateRequest;
import example.maintenance.estimate.customer.dto.request.VehicleCreateRequest;
import example.maintenance.estimate.customer.dto.request.VehicleUpdateRequest;
import example.maintenance.estimate.customer.entity.Customer;
import example.maintenance.estimate.customer.entity.CustomerAddress;
import example.maintenance.estimate.customer.entity.Vehicle;
import example.maintenance.estimate.customer.entity.enums.PlateRegion;
import example.maintenance.estimate.customer.helper.TestHelper;
import example.maintenance.estimate.customer.mapper.CustomerRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  CustomerRepository customerRepository;

  @InjectMocks
  CustomerService customerService;

  TestHelper testHelper;

  @BeforeEach
  void setup() {
    testHelper = new TestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 指定した車両番号４桁で検索すること() {
      Customer expectedCustomers = testHelper.customerMock().get(0);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(0);
      List<Vehicle> expectedVehicles = testHelper.vehicleMock()
          .stream()
          .filter(vehicle -> vehicle.getCustomerId() == expectedCustomers.getCustomerId())
          .toList();

      doReturn(expectedVehicles).when(customerRepository)
          .findVehicleByPlateNumber("1234");
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(expectedCustomers.getCustomerId());
      doReturn(Optional.of(expectedCustomers)).when(customerRepository)
          .findCustomerByCustomerId(expectedCustomers.getCustomerId());

      List<CustomerInformationDto> actualList = customerService.findInformationByPlateVehicleNumber(
          "1234");

      CustomerInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertCustomerInformation(actual, expectedCustomers, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 指定した名字や名前でユーザー情報を検索すること() {
      Customer expectedCustomer = testHelper.customerMock().get(0);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(0);
      List<Vehicle> expectedVehicles = testHelper.vehicleMock()
          .stream()
          .filter(vehicle -> vehicle.getCustomerId() == expectedCustomer.getCustomerId())
          .toList();
      doReturn(List.of(expectedCustomer)).when(customerRepository)
          .findCustomerByName("suzuki");
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(expectedCustomer.getCustomerId());
      doReturn(expectedVehicles).when(customerRepository)
          .findVehicleByCustomerId(expectedCustomer.getCustomerId());

      List<CustomerInformationDto> actualList = customerService.
          findCustomerInformation("suzuki", "", "");
      CustomerInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertCustomerInformation(actual, expectedCustomer, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 指定した読みかなでユーザー情報を検索すること() {
      Customer expectedCustomer = testHelper.customerMock().get(0);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(0);
      List<Vehicle> expectedVehicles = testHelper.vehicleMock()
          .stream()
          .filter(vehicle -> vehicle.getCustomerId() == expectedCustomer.getCustomerId())
          .toList();
      doReturn(List.of(expectedCustomer)).when(customerRepository)
          .findCustomerByNameKana("スズキ");
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(expectedCustomer.getCustomerId());
      doReturn(expectedVehicles).when(customerRepository)
          .findVehicleByCustomerId(expectedCustomer.getCustomerId());

      List<CustomerInformationDto> actualList = customerService.
          findCustomerInformation("", "スズキ", "");
      CustomerInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertCustomerInformation(actual, expectedCustomer, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 指定したemailでユーザー情報を検索すること() {
      Customer expectedCustomer = testHelper.customerMock().get(0);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(0);
      List<Vehicle> expectedVehicles = testHelper.vehicleMock()
          .stream()
          .filter(vehicle -> vehicle.getCustomerId() == expectedCustomer.getCustomerId())
          .toList();
      doReturn(Optional.of(expectedCustomer)).when(customerRepository)
          .findCustomerByEmail("ichiro@example.ne.jp");
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(expectedCustomer.getCustomerId());
      doReturn(expectedVehicles).when(customerRepository)
          .findVehicleByCustomerId(expectedCustomer.getCustomerId());

      List<CustomerInformationDto> actualList = customerService.
          findCustomerInformation("", "", "ichiro@example.ne.jp");
      CustomerInformationDto actual = actualList.get(0);
      assertThat(actualList).hasSize(1);
      assertCustomerInformation(actual, expectedCustomer, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 複数の車両情報を持つユーザーを正しく取得できること() {
      Customer expectedCustomer = testHelper.customerMock().get(1);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(1);
      List<Vehicle> expectedVehicles = testHelper.vehicleMock()
          .stream()
          .filter(vehicle -> vehicle.getCustomerId() == expectedCustomer.getCustomerId())
          .toList();
      doReturn(Optional.of(expectedCustomer)).when(customerRepository).findCustomerByCustomerId(2);
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(2);
      doReturn(expectedVehicles).when(customerRepository).findVehicleByCustomerId(2);

      List<CustomerInformationDto> userInfoList = customerService.findCustomerInformationById(2);
      CustomerInformationDto actual = userInfoList.get(0);
      assertCustomerInformation(actual, expectedCustomer, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 車両情報を持たないユーザーを正しく取得できること() {
      Customer expectedCustomer = testHelper.customerMock().get(2);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressMock().get(2);
      List<Vehicle> expectedVehicles = Collections.emptyList();

      doReturn(Optional.of(expectedCustomer)).when(customerRepository).findCustomerByCustomerId(3);
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(3);
      doReturn(expectedVehicles).when(customerRepository).findVehicleByCustomerId(3);

      List<CustomerInformationDto> userInfoList = customerService.findCustomerInformationById(3);
      CustomerInformationDto actual = userInfoList.get(0);
      assertCustomerInformation(actual, expectedCustomer, expectedCustomerAddress,
          expectedVehicles);
    }

    @Test
    void 存在しない車両番号でユーザー検索をしたとき空のリストを返すこと() {
      doReturn(Collections.emptyList()).when(customerRepository)
          .findVehicleByPlateNumber("0000");
      Assertions.assertThat(customerService.findInformationByPlateVehicleNumber("0000"))
          .isEmpty();
    }


    @Test
    void 存在しない名前で検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(customerRepository)
          .findCustomerByName("unknown");
      Assertions.assertThat(customerService.findCustomerInformation("unknown", "", ""))
          .isEmpty();
    }

    @Test
    void 存在しない電話番号で検索したとき空のリストをかえすこと() {
      doReturn(Optional.empty()).when(customerRepository)
          .findCustomerByPhoneNumber("000-0000-0000");
      Assertions.assertThat(customerService.findCustomerInformationByPhoneNumber("000-0000-0000"))
          .isEmpty();
    }

    @Test
    void 存在しない読みがなで検索したとき空のリストをかえすこと() {
      doReturn(Collections.emptyList()).when(customerRepository).findCustomerByNameKana("ヲ");
      Assertions.assertThat(customerService.findCustomerInformation("", "ヲ", ""))
          .isEmpty();
    }

    @Test
    void 存在しないEmailで検索したとき空のリストをかえすこと() {
      doReturn(Optional.empty()).when(customerRepository).findCustomerByEmail("unknown@test.jp");
      Assertions.assertThat(
              customerService.findCustomerInformation("", "", "unknown@test.jp"))
          .isEmpty();
    }

    @Test
    void 不正な検索パラメータで検索したときエラーが発生すること() {
      InvalidSearchParameterException expectedException = assertThrows(
          InvalidSearchParameterException.class, () -> {
            customerService.findCustomerInformation("", "", "");
          });
      assertEquals("Invalid search parameter", expectedException.getMessage());
    }

    private void assertCustomerInformation(CustomerInformationDto actual, Customer expectedCustomer,
        CustomerAddress expectedCustomerAddress,
        List<Vehicle> expectedVehicles) {
      Assertions.assertThat(actual.getCustomer().getCustomerId())
          .isEqualTo(expectedCustomer.getCustomerId());
      Assertions.assertThat(actual.getCustomer().getLastName()).isEqualTo(
          expectedCustomer.getLastName());
      Assertions.assertThat(actual.getCustomer().getFirstName())
          .isEqualTo(expectedCustomer.getFirstName());
      Assertions.assertThat(actual.getCustomer().getEmail()).isEqualTo(expectedCustomer.getEmail());
      Assertions.assertThat(actual.getCustomer().getPhoneNumber())
          .isEqualTo(expectedCustomer.getPhoneNumber());

      Assertions.assertThat(actual.getCustomerAddress().getAddressId()).isEqualTo(
          expectedCustomerAddress.getAddressId());
      Assertions.assertThat(actual.getCustomerAddress().getPostalCode()).isEqualTo(
          expectedCustomerAddress.getPostalCode());
      Assertions.assertThat(actual.getCustomerAddress().getPrefecture()).isEqualTo(
          expectedCustomerAddress.getPrefecture());
      Assertions.assertThat(actual.getCustomerAddress().getCity()).isEqualTo(
          expectedCustomerAddress.getCity());
      Assertions.assertThat(actual.getCustomerAddress().getTownAndNumber()).isEqualTo(
          expectedCustomerAddress.getTownAndNumber());
      assertThat(actual.getCustomerAddress().getBuildingNameAndRoomNumber()).isEqualTo(
          expectedCustomerAddress.getBuildingNameAndRoomNumber());
      Assertions.assertThat(actual.getVehicles()).isNotNull();
      Assertions.assertThat(actual.getVehicles()).hasSize(expectedVehicles.size());
      if (expectedVehicles.isEmpty()) {
        Assertions.assertThat(actual.getVehicles()).isEmpty();
        return;
      }
      for (int i = 0; i < expectedVehicles.size(); i++) {
        Vehicle actualVehicle = actual.getVehicles().get(i);
        Vehicle expectedVehicle = expectedVehicles.get(i);
        assertThat(actualVehicle.getVehicleId()).isEqualTo(expectedVehicle.getVehicleId());
        assertThat(actualVehicle.getCustomerId()).isEqualTo(expectedVehicle.getCustomerId());
        assertThat(actualVehicle.getPlateRegion()).isEqualTo(expectedVehicle.getPlateRegion());
        assertThat(actualVehicle.getPlateCategoryNumber()).isEqualTo(
            expectedVehicle.getPlateCategoryNumber());
        assertThat(actualVehicle.getPlateHiragana()).isEqualTo(expectedVehicle.getPlateHiragana());
        assertThat(actualVehicle.getPlateVehicleNumber()).isEqualTo(
            expectedVehicle.getPlateVehicleNumber());
        assertThat(actualVehicle.getMake()).isEqualTo(expectedVehicle.getMake());
        assertThat(actualVehicle.getModel()).isEqualTo(expectedVehicle.getModel());
        assertThat(actualVehicle.getType()).isEqualTo(expectedVehicle.getType());
        assertThat(actualVehicle.getYear()).isEqualTo(expectedVehicle.getYear());
        assertThat(actualVehicle.getInspectionDate()).isEqualTo(
            expectedVehicle.getInspectionDate());
      }
    }
  }

  @Nested
  class CreateClass {

    @Test
    public void 顧客を登録できること() {

      //準備
      CustomerCreateRequest customerCreateRequest = testHelper.customerCreateRequestMock();
      Customer expectedCustomer = testHelper.customerCreateMock();

      //モックの振る舞いを設定
      when(customerRepository.findCustomerByEmail(customerCreateRequest.getEmail())).thenReturn(
          Optional.empty());
      when(customerRepository.findCustomerByPhoneNumber(customerCreateRequest.getPhoneNumber()))
          .thenReturn(Optional.empty());
      doNothing().when(customerRepository).createCustomer(expectedCustomer);

      //実行
      Customer actual = customerService.registerCustomer(customerCreateRequest);

      //検証
      assertThat(actual).isEqualTo(expectedCustomer);
      assertThat(actual.getLastName()).isEqualTo(expectedCustomer.getLastName());
      assertThat(actual.getFirstName()).isEqualTo(expectedCustomer.getFirstName());
      verify(customerRepository, times(1)).createCustomer(expectedCustomer);
      verify(customerRepository, times(1)).findCustomerByEmail(customerCreateRequest.getEmail());
    }

    @Test
    void Eメールがすでに登録されていた場合顧客登録ができずエラーが発生すること() {
      //準備
      CustomerCreateRequest customerCreateRequest = testHelper.customerCreateRequestMock();
      Customer expectedCustomer = testHelper.customerCreateMock();

      //モックの振る舞いを設定
      when(customerRepository.findCustomerByEmail(customerCreateRequest.getEmail())).thenReturn(
          Optional.of(expectedCustomer));

      //実行
      AlreadyExistsEmailException exception = assertThrows(AlreadyExistsEmailException.class,
          () -> {
            customerService.registerCustomer(customerCreateRequest);
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Email already exists");
      verify(customerRepository, times(1)).findCustomerByEmail(customerCreateRequest.getEmail());
    }

    @Test
    void 電話番号がすでに登録されていた場合顧客登録ができずエラーが発生すること() {
      //準備
      CustomerCreateRequest customerCreateRequest = testHelper.customerCreateRequestMock();
      Customer expectedCustomer = testHelper.customerCreateMock();

      //モックの振る舞いを設定
      when(customerRepository.findCustomerByPhoneNumber(customerCreateRequest.getPhoneNumber()))
          .thenReturn(Optional.of(expectedCustomer));

      //実行
      AlreadyExistsPhoneNumberException exception = assertThrows(
          AlreadyExistsPhoneNumberException.class, () -> {
            customerService.registerCustomer(customerCreateRequest);
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Phone number already exists");
      verify(customerRepository, times(1)).findCustomerByPhoneNumber(
          customerCreateRequest.getPhoneNumber());
    }

    @Test
    void 顧客住所をIDに紐づけて登録できること() {
      Customer customer = testHelper.customerMock().get(0);
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressCreateMock(customer);
      CustomerAddressCreateRequest customerAddressCreateRequest = testHelper.customerAddressCreateRequestMock();

      //モックの振る舞いを設定
      doReturn(Optional.of(customer)).when(customerRepository)
          .findCustomerByCustomerId(customer.getCustomerId());
      doReturn(Optional.empty()).when(customerRepository)
          .findCustomerAddressByCustomerId(customer.getCustomerId());

      //実行
      CustomerAddress actual = customerService.registerCustomerAddress(customer.getCustomerId(),
          customerAddressCreateRequest);

      //検証
      assertThat(actual.getCustomerId()).isEqualTo(customer.getCustomerId());
      assertThat(actual.getPostalCode()).isEqualTo(expectedCustomerAddress.getPostalCode());
      assertThat(actual.getPrefecture()).isEqualTo(expectedCustomerAddress.getPrefecture());
      assertThat(actual.getCity()).isEqualTo(expectedCustomerAddress.getCity());
      assertThat(actual.getTownAndNumber()).isEqualTo(expectedCustomerAddress.getTownAndNumber());
      assertThat(actual.getBuildingNameAndRoomNumber()).isEqualTo(
          expectedCustomerAddress.getBuildingNameAndRoomNumber());

      assertCustomerAddress(actual, expectedCustomerAddress);
      verify(customerRepository, times(1)).findCustomerByCustomerId(customer.getCustomerId());
      verify(customerRepository, times(1)).findCustomerAddressByCustomerId(
          customer.getCustomerId());
      verify(customerRepository, times(1)).createCustomerAddress(expectedCustomerAddress);
    }

    @Test
    void 顧客登録されていないIDで住所を登録しようとするとエラーが発生すること() {
      //準備
      CustomerAddressCreateRequest customerAddressCreateRequest = testHelper.customerAddressCreateRequestMock();

      //モックの振る舞いを設定
      doReturn(Optional.empty()).when(customerRepository).findCustomerByCustomerId(0);

      //実行
      CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
        customerService.registerCustomerAddress(0, customerAddressCreateRequest);
      });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Not registered for customer ID:0");
      verify(customerRepository, times(1)).findCustomerByCustomerId(0);
    }

    @Test
    void 顧客住所がすでに登録されていたIDで登録しようとするとエラーが発生すること() {
      //準備
      Customer expectedUser = testHelper.customerMock().get(0);
      CustomerAddressCreateRequest customerAddressCreateRequest = testHelper.customerAddressCreateRequestMock();
      CustomerAddress expectedCustomerAddress = testHelper.customerAddressCreateMock(expectedUser);

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedUser)).when(customerRepository)
          .findCustomerByCustomerId(expectedUser.getCustomerId());
      doReturn(Optional.of(expectedCustomerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(expectedUser.getCustomerId());

      //実行
      CustomerAddressAlreadyException exception = assertThrows(
          CustomerAddressAlreadyException.class, () -> {
            customerService.registerCustomerAddress(expectedUser.getCustomerId(),
                customerAddressCreateRequest);
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo(
          "The customer with this ID has already completed address registration: "
              + expectedCustomerAddress.getCustomerId());
      verify(customerRepository, times(1)).findCustomerByCustomerId(expectedUser.getCustomerId());
    }

    //CustomerAddressオブジェクトのフィールド値を検証するメソッド
    private void assertCustomerAddress(CustomerAddress actual,
        CustomerAddress expectedCustomerAddress) {
      assertThat(actual.getCustomerId()).isEqualTo(expectedCustomerAddress.getCustomerId());
      assertThat(actual.getPostalCode()).isEqualTo(expectedCustomerAddress.getPostalCode());
      assertThat(actual.getPrefecture()).isEqualTo(expectedCustomerAddress.getPrefecture());
      assertThat(actual.getCity()).isEqualTo(expectedCustomerAddress.getCity());
      assertThat(actual.getTownAndNumber()).isEqualTo(expectedCustomerAddress.getTownAndNumber());
      assertThat(actual.getBuildingNameAndRoomNumber()).isEqualTo(
          expectedCustomerAddress.getBuildingNameAndRoomNumber());
    }

    @Test
    void 車両番号が重複していなければ車両情報が登録できること() {

      //準備
      Customer expectedCustomer = testHelper.customerMock().get(0);
      Vehicle expectedVehicle = testHelper.vehicleMock().get(0);
      VehicleCreateRequest vehicleCreateRequest = testHelper.vehicleCreateRequestMock();

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedCustomer)).when(customerRepository)
          .findCustomerByCustomerId(expectedCustomer.getCustomerId());
      doReturn(Optional.empty()).when(customerRepository)
          .findVehicleByLicensePlateExactMatch(
              expectedVehicle.getPlateRegion(),
              expectedVehicle.getPlateCategoryNumber(),
              expectedVehicle.getPlateHiragana(),
              expectedVehicle.getPlateVehicleNumber());

      //実行
      Vehicle actual = customerService.registerVehicle(
          expectedCustomer.getCustomerId(), vehicleCreateRequest);

      //検証
      assertVehicle(actual, expectedVehicle);
      verify(customerRepository, times(1)).findVehicleByLicensePlateExactMatch(
          expectedVehicle.getPlateRegion(),
          expectedVehicle.getPlateCategoryNumber(),
          expectedVehicle.getPlateHiragana(),
          expectedVehicle.getPlateVehicleNumber());
      verify(customerRepository, times(1)).createVehicle(expectedVehicle);
    }

    @Test
    void 車両年式が未来の場合エラーが発生すること() {
      //準備
      Customer expectedCustomer = testHelper.customerMock().get(0);
      VehicleCreateRequest vehicleCreateRequest = testHelper.vehicleCreateRequestMock();
      vehicleCreateRequest.setYear("2028-01");

      //モックの振る舞いを設定
      doReturn(Optional.of(expectedCustomer)).when(customerRepository)
          .findCustomerByCustomerId(expectedCustomer.getCustomerId());

      //実行
      VehicleYearInvalidException exception = assertThrows(VehicleYearInvalidException.class,
          () -> {
            customerService.registerVehicle(expectedCustomer.getCustomerId(), vehicleCreateRequest);
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Vehicle year is invalid.");
    }

    //Vehicleオブジェクトのフィールド値を検証するメソッド
    private void assertVehicle(Vehicle actual, Vehicle expectedVehicle) {
      assertThat(actual.getVehicleId()).isNotNull();
      assertThat(actual.getCustomerId()).isEqualTo(expectedVehicle.getCustomerId());
      assertThat(actual.getPlateRegion()).isEqualTo(expectedVehicle.getPlateRegion());
      assertThat(actual.getPlateCategoryNumber()).isEqualTo(
          expectedVehicle.getPlateCategoryNumber());
      assertThat(actual.getPlateHiragana()).isEqualTo(expectedVehicle.getPlateHiragana());
      assertThat(actual.getPlateVehicleNumber()).isEqualTo(
          expectedVehicle.getPlateVehicleNumber());
      assertThat(actual.getMake()).isEqualTo(expectedVehicle.getMake());
      assertThat(actual.getModel()).isEqualTo(expectedVehicle.getModel());
      assertThat(actual.getType()).isEqualTo(expectedVehicle.getType());
      assertThat(actual.getYear()).isEqualTo(expectedVehicle.getYear());
      assertThat(actual.getInspectionDate()).isEqualTo(expectedVehicle.getInspectionDate());
    }
  }

  @Nested
  class DeleteClass {

    @Test
    void 顧客情報を削除できること() {
      //準備
      Customer expectedCustomer = testHelper.customerMock().get(0);
      doReturn(Optional.of(expectedCustomer)).when(customerRepository)
          .findCustomerByCustomerId(expectedCustomer.getCustomerId());
      doNothing().when(customerRepository).deleteCustomer(expectedCustomer.getCustomerId());

      //実行
      customerService.deleteCustomerByCustomerId(expectedCustomer.getCustomerId());

      //検証
      verify(customerRepository, times(1)).deleteCustomer(expectedCustomer.getCustomerId());
      verify(customerRepository, times(1)).findCustomerByCustomerId(
          expectedCustomer.getCustomerId());
    }

    @Test
    void 車両情報を非アクティブにして削除できること() {
      //準備
      Vehicle expectedVehicle = testHelper.vehicleMock().get(0);
      doReturn(Optional.of(expectedVehicle)).when(customerRepository)
          .findVehicleByVehicleId(expectedVehicle.getVehicleId());
      doNothing().when(customerRepository).deactivateVehicle(expectedVehicle.getVehicleId());

      //実行
      customerService.deleteVehicleByVehicleId(expectedVehicle.getVehicleId());

      //検証
      verify(customerRepository, times(1)).deactivateVehicle(expectedVehicle.getVehicleId());
      verify(customerRepository, times(1)).findVehicleByVehicleId(
          expectedVehicle.getVehicleId());
    }

    @Test
    void 車両情報がすでに非アクティブの場合エラーが発生すること() {
      //準備
      Vehicle expectedVehicle = new Vehicle();
      expectedVehicle.setVehicleId(1);
      expectedVehicle.setActive(false);
      doReturn(Optional.of(expectedVehicle)).when(customerRepository)
          .findVehicleByVehicleId(expectedVehicle.getVehicleId());

      //実行
      VehicleException.VehicleInactiveException exception = assertThrows(
          VehicleException.VehicleInactiveException.class, () -> {
            customerService.deleteVehicleByVehicleId(expectedVehicle.getVehicleId());
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Vehicle is already inactive.");
    }
  }

  @Nested
  class UpdateClass {


    @Test
    void 指定したIDで顧客情報を更新できること() {
      //準備
      Customer customer = new Customer();
      customer.setCustomerId(1);
      customer.setPhoneNumber("090-1234-5678");
      customer.setEmail("test@example.com");

      CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
      customerUpdateRequest.setPhoneNumber("090-9876-5432");
      customerUpdateRequest.setEmail("new@example.com");

      //モックの振る舞いを設定
      doReturn(Optional.of(customer)).when(customerRepository).findCustomerByCustomerId(1);
      // 重複なし
      doReturn(Optional.empty()).when(
          customerRepository).findCustomerByPhoneNumber("090-9876-5432");
      doReturn(Optional.empty()).when(customerRepository).findCustomerByEmail("new@example.com");

      // 実行
      customerService.updateCustomerByCustomerId(1, customerUpdateRequest);

      // 検証
      verify(customerRepository).updateCustomer(customer);
    }

    @Test
    void 顧客住所を更新できること() {
      //準備
      Customer customer = new Customer();
      customer.setCustomerId(1);

      CustomerAddress customerAddress = new CustomerAddress();

      CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest();
      customerAddressUpdateRequest.setPostalCode("987-6543");
      customerAddressUpdateRequest.setPrefecture("大阪府");
      customerAddressUpdateRequest.setCity("大阪市");
      customerAddressUpdateRequest.setTownAndNumber("梅田1-1-1");
      customerAddressUpdateRequest.setBuildingNameAndRoomNumber("梅田ビル202");

      doReturn(Optional.of(customer)).when(customerRepository)
          .findCustomerByCustomerId(customer.getCustomerId());
      doReturn(Optional.of(customerAddress)).when(customerRepository)
          .findCustomerAddressByCustomerId(customer.getCustomerId());

      customerService.updateCustomerAddressByCustomerId(1, customerAddressUpdateRequest);
      //検証
      verify(customerRepository, times(1)).updateCustomerAddress(customerAddress);
    }

    @Test
    void 車両情報を更新できること() {
      //準備
      Vehicle vehicle = new Vehicle();
      vehicle.setVehicleId(1);
      vehicle.setPlateRegion(PlateRegion.相模);
      vehicle.setPlateCategoryNumber("500");
      vehicle.setPlateHiragana("あ");
      vehicle.setPlateVehicleNumber("1234");
      vehicle.setMake("toyota");
      vehicle.setModel("NZE141");
      vehicle.setType("EF-NZE141");
      vehicle.setYear(YearMonth.of(2020, 10));
      vehicle.setInspectionDate(LocalDate.parse("2023-10-01"));

      VehicleUpdateRequest vehicleUpdateRequest = new VehicleUpdateRequest();
      vehicleUpdateRequest.setPlateRegion("湘南");
      vehicleUpdateRequest.setPlateCategoryNumber("500");
      vehicleUpdateRequest.setPlateHiragana("あ");
      vehicleUpdateRequest.setPlateVehicleNumber("1234");
      vehicleUpdateRequest.setYear("2020-10");
      vehicleUpdateRequest.setMake("toyota");
      vehicleUpdateRequest.setModel("NZE141");
      vehicleUpdateRequest.setType("EF-NZE141");
      vehicleUpdateRequest.setInspectionDate("2025-10-01");

      //モックの振る舞いを設定
      doReturn(Optional.of(vehicle)).when(customerRepository)
          .findVehicleByVehicleId(1);
      doReturn(Optional.of(vehicle)).when(customerRepository)
          .findVehicleByLicensePlateExactMatch(
              PlateRegion.valueOf(vehicleUpdateRequest.getPlateRegion()),
              vehicleUpdateRequest.getPlateCategoryNumber(),
              vehicleUpdateRequest.getPlateHiragana(),
              vehicleUpdateRequest.getPlateVehicleNumber());
      //実行
      customerService.updateVehicleByVehicleId(1, vehicleUpdateRequest);
      //検証
      verify(customerRepository, times(1)).updateVehicle(vehicle);
    }

    @Test
    void 更新時に車両番号が重複している場合エラーが発生すること() {
      //準備
      Vehicle vehicle = new Vehicle();
      vehicle.setVehicleId(99);
      vehicle.setPlateRegion(PlateRegion.湘南);
      vehicle.setPlateCategoryNumber("500");
      vehicle.setPlateHiragana("あ");
      vehicle.setPlateVehicleNumber("1234");

      Vehicle selfVehicle = new Vehicle();
      selfVehicle.setVehicleId(1);

      VehicleUpdateRequest vehicleUpdateRequest = new VehicleUpdateRequest();
      vehicleUpdateRequest.setPlateRegion("湘南");
      vehicleUpdateRequest.setPlateCategoryNumber("500");
      vehicleUpdateRequest.setPlateHiragana("あ");
      vehicleUpdateRequest.setPlateVehicleNumber("1234");
      vehicleUpdateRequest.setYear("2020-10");
      vehicleUpdateRequest.setMake("toyota");
      vehicleUpdateRequest.setModel("NZE141");
      vehicleUpdateRequest.setType("EF-NZE141");
      vehicleUpdateRequest.setInspectionDate("2025-10-01");

      //モックの振る舞い
      doReturn(Optional.of(selfVehicle)).when(customerRepository)
          .findVehicleByVehicleId(1);
      doReturn(Optional.of(vehicle)).when(customerRepository)
          .findVehicleByLicensePlateExactMatch(
              PlateRegion.valueOf(vehicleUpdateRequest.getPlateRegion()),
              vehicleUpdateRequest.getPlateCategoryNumber(),
              vehicleUpdateRequest.getPlateHiragana(),
              vehicleUpdateRequest.getPlateVehicleNumber());

      //実行
      VehicleException.AlreadyExistsVehicleException exception = assertThrows(
          VehicleException.AlreadyExistsVehicleException.class, () -> {
            customerService.updateVehicleByVehicleId(1, vehicleUpdateRequest);
          });

      //検証
      assertThat(exception.getMessage()).isEqualTo("Vehicle already exists");
    }
  }
}
