package com.u1pena.estimateapi.customer.converter.customerInformation;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.common.enums.PlateRegion;
import com.u1pena.estimateapi.customer.converter.VehicleCreateConverter;
import com.u1pena.estimateapi.customer.dto.request.VehicleCreateRequest;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.service.CustomerService;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
