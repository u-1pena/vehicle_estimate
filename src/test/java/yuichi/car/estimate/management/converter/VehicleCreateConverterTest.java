package yuichi.car.estimate.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yuichi.car.estimate.management.dto.request.VehicleCreateRequest;
import yuichi.car.estimate.management.entity.Customer;
import yuichi.car.estimate.management.entity.Vehicle;
import yuichi.car.estimate.management.entity.enums.PlateRegion;
import yuichi.car.estimate.management.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class VehicleCreateConverterTest {

  @Mock
  CustomerService customerService;

  @Test
  void VehicleCreateRequestをVehicleに変換できること() {

    Customer customer = new Customer();
    VehicleCreateRequest vehicleCreateRequest = new VehicleCreateRequest();
    vehicleCreateRequest.setPlateRegion("品川");
    vehicleCreateRequest.setPlateCategoryNumber("300");
    vehicleCreateRequest.setPlateHiragana("あ");
    vehicleCreateRequest.setPlateVehicleNumber("1234");
    vehicleCreateRequest.setMake("toyota");
    vehicleCreateRequest.setModel("NZE-141");
    vehicleCreateRequest.setType("1AZ-FE");
    vehicleCreateRequest.setYear("2021-12");
    vehicleCreateRequest.setInspectionDate("2027-01-01");

    Vehicle actual = VehicleCreateConverter.vehicleConvertToEntity(customer, vehicleCreateRequest);

    assertThat(actual.getCustomerId()).isEqualTo(customer.getCustomerId());
    assertThat(actual.getPlateRegion()).isEqualTo(PlateRegion.品川);
    assertThat(actual.getPlateCategoryNumber()).isEqualTo("300");
    assertThat(actual.getPlateHiragana()).isEqualTo("あ");
    assertThat(actual.getPlateVehicleNumber()).isEqualTo("1234");
    assertThat(actual.getMake()).isEqualTo("toyota");
    assertThat(actual.getModel()).isEqualTo("NZE-141");
    assertThat(actual.getType()).isEqualTo("1AZ-FE");
    assertThat(actual.getYear()).isEqualTo(YearMonth.parse("2021-12"));
    assertThat(actual.getInspectionDate()).isEqualTo(LocalDate.parse("2027-01-01"));
  }
}
