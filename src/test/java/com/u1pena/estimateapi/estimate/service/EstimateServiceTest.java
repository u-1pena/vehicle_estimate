package com.u1pena.estimateapi.estimate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.exception.VehicleException;
import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductCreateRequest;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.estimate.exception.EstimateException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.ExistOilFilterProductsException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.ExistOilProductsException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.NoMatchMaintenanceGuideException;
import com.u1pena.estimateapi.estimate.helper.EstimateTestHelper;
import com.u1pena.estimateapi.estimate.repository.EstimateRepository;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.helper.MasterTestHelper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EstimateServiceTest {

  @Mock
  EstimateRepository estimateRepository;

  @InjectMocks
  EstimateService estimateService;
  EstimateTestHelper estimateTestHelper;
  CustomerTestHelper customerTestHelper;
  MasterTestHelper masterTestHelper;

  @BeforeEach
  void setup() {
    estimateTestHelper = new EstimateTestHelper();
    customerTestHelper = new CustomerTestHelper();
    masterTestHelper = new MasterTestHelper();
  }

  @Nested
  class ReadClass {

    @Test
    void 見積もりIDからヘッダーと詳細を取得しEstimateFullResponseを取得する() {
      //準備
      EstimateBase estimateBase = EstimateBase.builder()
          .estimateBaseId(1)
          .estimateDate(LocalDate.of(2025, 1, 1))
          .customerId(1)
          .vehicleId(1)
          .build();
      Customer customer = customerTestHelper.customerMock().get(0);
      CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(0);
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);

      EstimateHeaderResponse estimateHeaderResponse = estimateTestHelper
          .estimateHeaderResponseMock();

      List<EstimateProductResponse> estimateProductResponse = estimateTestHelper
          .estimateProductResponseMock();

      BigDecimal totalPrice = estimateProductResponse.stream()
          .map(EstimateProductResponse::getTotalPrice)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      List<EstimateProduct> estimateProducts = estimateTestHelper.estimateProductsMock();
      List<EstimateProductJoinResult> joinResults = estimateTestHelper
          .joinResultsMock();

      doReturn(Optional.of(estimateBase)).when(estimateRepository)
          .findEstimateBaseById(estimateHeaderResponse.getEstimateBaseId());
      doReturn(Optional.of(customer)).when(estimateRepository)
          .findCustomerById(estimateBase.getCustomerId());
      doReturn(Optional.of(customerAddress)).when(estimateRepository)
          .findCustomerAddressByCustomerId(customerAddress.getCustomerId());
      doReturn(Optional.of(vehicle)).when(estimateRepository)
          .findVehicleByVehicleId(estimateBase.getVehicleId());
      doReturn(Optional.of("カローラアクシオ")).when(estimateRepository)
          .findVehicleNameByMaintenanceId(estimateBase.getMaintenanceId());
      doReturn(estimateProducts).when(estimateRepository)
          .findEstimateProductsByEstimateBaseId(estimateBase.getEstimateBaseId());
      doReturn(joinResults).when(estimateRepository)
          .findProductsWithCategoryByIds(List.of(1, 2));

      //実行
      EstimateFullResponse actual = estimateService.getEstimateFullById(1);

      //検証
      assertThat(actual.getEstimateHeader()).isEqualTo(estimateHeaderResponse);
      assertThat(actual.getEstimateProducts()).isEqualTo(estimateProductResponse);
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(totalPrice);
    }

    @Test
    void 見積もりベースのみで商品見積もりが存在しない場合は空のリストを返す() {
      // 準備
      EstimateBase estimateBase = EstimateBase.builder()
          .estimateBaseId(1)
          .estimateDate(LocalDate.of(2025, 1, 1))
          .customerId(1)
          .vehicleId(1)
          .build();
      Customer customer = customerTestHelper.customerMock().get(0);
      CustomerAddress customerAddress = customerTestHelper.customerAddressMock().get(0);
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);

      EstimateHeaderResponse estimateHeaderResponse = estimateTestHelper
          .estimateHeaderResponseMock();

      List<EstimateProductJoinResult> joinResults = estimateTestHelper
          .joinResultsMock();
      doReturn(Optional.of(estimateBase)).when(estimateRepository)
          .findEstimateBaseById(estimateHeaderResponse.getEstimateBaseId());
      doReturn(Optional.of(customer)).when(estimateRepository)
          .findCustomerById(estimateBase.getCustomerId());
      doReturn(Optional.of(customerAddress)).when(estimateRepository)
          .findCustomerAddressByCustomerId(customer.getCustomerId());
      doReturn(Optional.of(vehicle)).when(estimateRepository)
          .findVehicleByVehicleId(estimateBase.getVehicleId());
      doReturn(Optional.of("カローラアクシオ")).when(estimateRepository)
          .findVehicleNameByMaintenanceId(estimateBase.getMaintenanceId());
      doReturn(Collections.emptyList()).when(estimateRepository)
          .findEstimateProductsByEstimateBaseId(estimateBase.getEstimateBaseId());
      // 実行
      EstimateFullResponse actual = estimateService.getEstimateFullById(1);
      // 検証
      assertThat(actual.getEstimateHeader()).isEqualTo(estimateHeaderResponse);
      assertThat(actual.getEstimateProducts()).isEmpty();
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    }


    @Test
    void 見積もりの概要をリストで顧客IDから取得する() {
      //　準備
      List<EstimateSummaryResult> results = List.of(
          EstimateSummaryResult.builder()
              .estimateBaseId(1)
              .estimateDate(LocalDate.of(2023, 10, 1))
              .vehicleName("カローラ")
              .totalPrice(new BigDecimal("10000"))
              .build(),
          EstimateSummaryResult.builder()
              .estimateBaseId(2)
              .estimateDate(LocalDate.of(2023, 10, 2))
              .vehicleName("カローラ")
              .totalPrice(new BigDecimal("15000"))
              .build());

      doReturn(results).when(estimateRepository)
          .findEstimateSummaryResultsByCustomerId(1);

      // 実行
      List<EstimateSummaryResponse> actual = estimateService
          .getEstimateSummaryByCustomerId(1);

      //　検証
      assertThat(actual).hasSize(2);
      assertThat(actual.get(0).getEstimateBaseId()).isEqualTo(1);
      assertThat(actual.get(0).getEstimateDate()).isEqualTo(LocalDate.of(2023, 10, 1));
      assertThat(actual.get(0).getVehicleName()).isEqualTo("カローラ");
      assertThat(actual.get(0).getTotalPrice()).isEqualByComparingTo(new BigDecimal("10000"));
      assertThat(actual.get(1).getEstimateBaseId()).isEqualTo(2);
      assertThat(actual.get(1).getEstimateDate()).isEqualTo(LocalDate.of(2023, 10, 2));
      assertThat(actual.get(1).getVehicleName()).isEqualTo("カローラ");
      assertThat(actual.get(1).getTotalPrice()).isEqualByComparingTo(new BigDecimal("15000"));
    }

    @Test
    void 指定した期間内の見積もりの概要をリストで取得する() {
      // 準備
      LocalDate startDate = LocalDate.of(2023, 10, 1);
      LocalDate endDate = LocalDate.of(2023, 10, 31);
      List<EstimateSummaryResult> results = List.of(
          EstimateSummaryResult.builder()
              .estimateBaseId(1)
              .estimateDate(LocalDate.of(2023, 10, 1))
              .vehicleName("カローラ")
              .totalPrice(new BigDecimal("10000"))
              .build(),
          EstimateSummaryResult.builder()
              .estimateBaseId(2)
              .estimateDate(LocalDate.of(2023, 10, 15))
              .vehicleName("カローラ")
              .totalPrice(new BigDecimal("15000"))
              .build());

      doReturn(results).when(estimateRepository)
          .findEstimateSummaryResultsByDateRange(startDate.toString(), endDate.toString());

      // 実行
      List<EstimateSummaryDateResponse> actual = estimateService
          .getEstimatesByDateRange(startDate, endDate);

      // 検証
      assertThat(actual).hasSize(2);
      assertThat(actual.get(0).getEstimateBaseId()).isEqualTo(1);
      assertThat(actual.get(0).getEstimateDate()).isEqualTo(LocalDate.of(2023, 10, 1));
      assertThat(actual.get(0).getVehicleName()).isEqualTo("カローラ");
      assertThat(actual.get(0).getTotalPrice()).isEqualByComparingTo(new BigDecimal("10000"));
      assertThat(actual.get(1).getEstimateBaseId()).isEqualTo(2);
      assertThat(actual.get(1).getEstimateDate()).isEqualTo(LocalDate.of(2023, 10, 15));
      assertThat(actual.get(1).getVehicleName()).isEqualTo("カローラ");
      assertThat(actual.get(1).getTotalPrice()).isEqualByComparingTo(new BigDecimal("15000"));
    }

    @Test
    void 見積もりベースが存在しない場合は例外をスローする() {
      // 準備
      doReturn(Optional.empty()).when(estimateRepository)
          .findEstimateBaseById(1);
      // 実行
      EstimateException.EstimateBaseNotFoundException exception = assertThrows(
          EstimateException.EstimateBaseNotFoundException.class,
          () -> estimateService.getEstimateFullById(1));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new EstimateException.EstimateBaseNotFoundException().getMessage());
    }
  }

  @Nested
  class CreateClass {

    @Test
    void 見積もりベースを登録できる() {
      // 準備
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);
      EstimateBase expectedEstimateBase = EstimateBase.builder()
          .estimateBaseId(1)
          .customerId(vehicle.getCustomerId())
          .vehicleId(vehicle.getVehicleId())
          .maintenanceId(1)
          .estimateDate(LocalDate.of(2023, 10, 1))
          .build();
      MaintenanceGuide maintenanceGuide = MaintenanceGuide.builder()
          .maintenanceId(1)
          .make("toyota")
          .model("DBA-NZE141")
          .type("1NZ")
          .startYear(YearMonth.of(2006, 10))
          .endYear(YearMonth.of(2012, 5))
          .oilViscosity("0w-20")
          .oilQuantityWithFilter(3.7)
          .oilQuantityWithoutFilter(3.4)
          .oilFilterPartNumber("90915-10003")
          .carWashSize(CarWashSize.M)
          .build();
      doReturn(Optional.of(vehicle)).when(estimateRepository).findVehicleByVehicleId(1);
      doReturn(Optional.of(maintenanceGuide)).when(estimateRepository)
          .findMaintenanceGuideByMakeAndModelAndYear(
              vehicle.getMake(), vehicle.getModel(), vehicle.getYear());
      doAnswer(invocation -> {
        EstimateBase estimateBase = invocation.getArgument(0);
        estimateBase.setEstimateBaseId(expectedEstimateBase.getEstimateBaseId());
        return null;
      }).when(estimateRepository).insertEstimateBase(any(EstimateBase.class));

      // 実行
      int actual = estimateService.registerEstimateBase(1);
      // 検証
      assertThat(actual).isEqualTo(expectedEstimateBase.getEstimateBaseId());
    }

    @Test
    void オイルフィルターなし_ユーザー指定なしならガイド数量が返る() {
      // 準備
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();

      double actual = estimateService.resolveQuantityByCategory(1, 1,
          1, guideProductPermission, null);
      // 検証
      assertThat(actual).isEqualTo(3.0);
    }

    @Test
    void CATEGORY_OIL_フィルターなし_ユーザー指定ありならユーザー指定数量が返る() {
      // 準備
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();

      double actual = estimateService.resolveQuantityByCategory(1, 1,
          1, guideProductPermission, 5.0);
      // 検証
      assertThat(actual).isEqualTo(5.0);
    }

    @Test
    void フィルターあり_ユーザー指定なしならフィルター込数量が返る() {
      // 準備
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();
      MaintenanceGuide maintenanceGuide = MaintenanceGuide.builder()
          .oilQuantityWithFilter(3.7)
          .build();
      doReturn(true).when(estimateRepository)
          .existOilFilterProductsByEstimateBaseId(1);
      doReturn(maintenanceGuide.getOilQuantityWithFilter()).when(estimateRepository)
          .findOilQuantityWithFilterByMaintenanceId(1);

      double actual = estimateService.resolveQuantityByCategory(1, 1,
          1, guideProductPermission, null);
      // 検証
      assertThat(actual).isEqualTo(3.7);
    }

    @Test
    void CATEGORY_OIL_フィルターあり_ユーザー指定ありならユーザー指定数量が返る() {
      // 準備
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();
      MaintenanceGuide maintenanceGuide = MaintenanceGuide.builder()
          .oilQuantityWithFilter(3.7)
          .build();

      double actual = estimateService.resolveQuantityByCategory(1, 1,
          1, guideProductPermission, 5.0);
      // 検証
      assertThat(actual).isEqualTo(5.0);
    }

    @Test
    void オイル商品を登録すると数量をガイドから取得し見積もり商品として登録できる() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(1)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilMock().get(0);
      GuideProductPermission guideProductPermission = estimateTestHelper.guideProductPermissionMock();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);

      // 実行
      estimateService.registerEstimateProduct(1, estimateProductCreateRequest);
      // 検証
      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).insertEstimateProduct(captor.capture());

      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBase.getEstimateBaseId());
      assertThat(actual.getProductId()).isEqualTo(product.getProductId());
      assertThat(actual.getQuantity()).isEqualTo(guideProductPermission.getQuantity());
      assertThat(actual.getUnitPrice()).isEqualByComparingTo(product.getPrice());
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(
          product.getPrice().multiply(BigDecimal.valueOf(actual.getQuantity())));
    }

    @Test
    void 既にオイルフィルターが存在する場合ガイドからオイルフィルター込の数量を取得し見積もり商品として登録できる() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(1)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilMock().get(0);
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .maintenanceId(1)
          .productId(1)
          .categoryId(1) // CATEGORY_OIL
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();

      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(true).when(estimateRepository)
          .existOilFilterProductsByEstimateBaseId(estimateBase.getEstimateBaseId());

      doReturn(3.7).when(estimateRepository)
          .findOilQuantityWithFilterByMaintenanceId(estimateBase.getMaintenanceId());

      // 実行
      estimateService.registerEstimateProduct(1, estimateProductCreateRequest);
      // 検証
      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).insertEstimateProduct(captor.capture());

      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBase.getEstimateBaseId());
      assertThat(actual.getProductId()).isEqualTo(product.getProductId());
      assertThat(actual.getQuantity()).isEqualTo(3.7);
      assertThat(actual.getUnitPrice()).isEqualByComparingTo(product.getPrice());
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(
          product.getPrice().multiply(BigDecimal.valueOf(actual.getQuantity())));
    }

    @Test
    void ユーザーが数量を指定した場合はその数量で見積もり商品を登録できる() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(1)
          .quantity(5.0) // ユーザーが指定した数量
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilMock().get(0);
      GuideProductPermission guideProductPermission = estimateTestHelper.guideProductPermissionMock();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);

      // 実行
      estimateService.registerEstimateProduct(1, estimateProductCreateRequest);
      // 検証
      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).insertEstimateProduct(captor.capture());

      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBase.getEstimateBaseId());
      assertThat(actual.getProductId()).isEqualTo(product.getProductId());
      assertThat(actual.getQuantity()).isEqualTo(5.0); // ユーザーが指定した数量
      assertThat(actual.getUnitPrice()).isEqualByComparingTo(product.getPrice());
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(
          product.getPrice().multiply(BigDecimal.valueOf(actual.getQuantity())));
    }

    @Test
    void オイルフィルター商品を見積もり商品として登録できる() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(11)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilFilterMock().get(0);
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .maintenanceId(1)
          .productId(11)
          .categoryId(2) // CATEGORY_OIL_FILTER
          .quantity(1.0)
          .autoAdjustQuantity(true)
          .build();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(false).when(estimateRepository)
          .existOilFilterProductsByEstimateBaseId(estimateBase.getEstimateBaseId());
      // 実行
      estimateService.registerEstimateProduct(1, estimateProductCreateRequest);
      // 検証
      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).insertEstimateProduct(captor.capture());

      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBase.getEstimateBaseId());
      assertThat(actual.getProductId()).isEqualTo(product.getProductId());
      assertThat(actual.getQuantity()).isEqualTo(1.0);
      assertThat(actual.getUnitPrice()).isEqualByComparingTo(product.getPrice());
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(
          product.getPrice().multiply(BigDecimal.valueOf(actual.getQuantity())));
    }

    @Test
    void オイル登録済みの状態でオイルフィルターを追加するとオイル数量がフィルター込の数量に更新されオイルフィルターが登録される() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(11)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilFilterMock().get(0);
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .maintenanceId(1)
          .productId(11)
          .categoryId(2) // CATEGORY_OIL_FILTER
          .quantity(1.0)
          .autoAdjustQuantity(true)
          .build();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(1).when(estimateRepository)
          .findProductWithOilCategoryByEstimateBaseId(estimateBase.getEstimateBaseId());
      doReturn(true).when(estimateRepository)
          .existOilProductsByEstimateBaseId(estimateBase.getEstimateBaseId());
      doReturn(3.7).when(estimateRepository)
          .findOilQuantityWithFilterByMaintenanceId(estimateBase.getMaintenanceId());
      doReturn(1).when(estimateRepository)
          .findEstimateProductIdByEstimateBaseIdAndProductId(estimateBase.getEstimateBaseId(), 1);

      // 実行
      estimateService.registerEstimateProduct(1, estimateProductCreateRequest);
      // 検証
      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).insertEstimateProduct(captor.capture());

      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateBaseId()).isEqualTo(estimateBase.getEstimateBaseId());
      assertThat(actual.getProductId()).isEqualTo(product.getProductId());
      assertThat(actual.getQuantity()).isEqualTo(1.0);
      assertThat(actual.getUnitPrice()).isEqualByComparingTo(product.getPrice());
      assertThat(actual.getTotalPrice()).isEqualByComparingTo(
          product.getPrice().multiply(BigDecimal.valueOf(actual.getQuantity())));
      // オイル数量の更新が行われたことを確認
      verify(estimateRepository).updateOilQuantityWithEstimateProductId(
          1, 3.7);
    }

    @Test
    void オイルが既に登録済みの状態でオイルを登録しようとすると重複エラーが発生する() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(1)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilMock().get(0);
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .maintenanceId(1)
          .productId(1)
          .categoryId(1) // CATEGORY_OIL
          .quantity(3.0)
          .autoAdjustQuantity(true)
          .build();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(true).when(estimateRepository)
          .existOilProductsByEstimateBaseId(estimateBase.getEstimateBaseId());

      // 実行
      EstimateException.ExistOilProductsException exception = assertThrows(
          ExistOilProductsException.class,
          () -> estimateService.registerEstimateProduct(1, estimateProductCreateRequest));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(new ExistOilProductsException().getMessage());
      verify(estimateRepository).existOilProductsByEstimateBaseId(
          estimateBase.getEstimateBaseId());
    }

    @Test
    void オイルフィルターが既に登録済みの状態でオイルフィルターを登録しようとすると重複エラーが発生する() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(11)
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilFilterMock().get(0);
      GuideProductPermission guideProductPermission = GuideProductPermission.builder()
          .maintenanceId(1)
          .productId(11)
          .categoryId(2) // CATEGORY_OIL_FILTER
          .quantity(1.0)
          .autoAdjustQuantity(true)
          .build();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(true).when(estimateRepository)
          .existOilFilterProductsByEstimateBaseId(estimateBase.getEstimateBaseId());
      // 実行
      EstimateException.ExistOilFilterProductsException exception = assertThrows(
          ExistOilFilterProductsException.class,
          () -> estimateService.registerEstimateProduct(1, estimateProductCreateRequest));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new ExistOilFilterProductsException().getMessage());
      verify(estimateRepository).existOilFilterProductsByEstimateBaseId(
          estimateBase.getEstimateBaseId());
    }

    private void doReturnEstimateProductRegisterStub(
        EstimateBase estimateBase, Product product, GuideProductPermission guideProductPermission) {
      doReturn(Optional.of(estimateBase)).when(estimateRepository)
          .findEstimateBaseById(estimateBase.getEstimateBaseId());
      doReturn(Optional.of(product)).when(estimateRepository)
          .findProductById(product.getProductId());
      doReturn(Optional.of(guideProductPermission)).when(estimateRepository)
          .findPermissionByMaintenanceIdAndProductId(
              estimateBase.getMaintenanceId(), product.getProductId());
    }

    @Test
    void 車両ガイドに存在しない商品を登録しようとするとエラーを返す() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(2) // ガイドにない商品ID
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      doReturn(Optional.of(estimateBase)).when(estimateRepository)
          .findEstimateBaseById(estimateBase.getEstimateBaseId());
      doReturn(Optional.empty()).when(estimateRepository).findPermissionByMaintenanceIdAndProductId(
          estimateBase.getMaintenanceId(), estimateProductCreateRequest.getProductId());
      // 実行
      EstimateException.NoMatchPermissionException exception = assertThrows(
          EstimateException.NoMatchPermissionException.class,
          () -> estimateService.registerEstimateProduct(1, estimateProductCreateRequest));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new EstimateException.NoMatchPermissionException().getMessage());
    }

    @Test
    void 車両情報が存在しない場合はエラーを返す() {
      // 準備

      doReturn(Optional.empty()).when(estimateRepository).findVehicleByVehicleId(1);
      // 実行
      VehicleException.VehicleNotFoundException exception = assertThrows(
          VehicleException.VehicleNotFoundException.class,
          () -> estimateService.registerEstimateBase(1));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new VehicleException.VehicleNotFoundException().getMessage());
    }

    @Test
    void メンテナンス情報が存在しない場合はエラーを返す() {
      // 準備
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);
      doReturn(Optional.of(vehicle)).when(estimateRepository).findVehicleByVehicleId(1);
      doReturn(Optional.empty()).when(estimateRepository)
          .findMaintenanceGuideByMakeAndModelAndYear(
              vehicle.getMake(), vehicle.getModel(), vehicle.getYear());
      // 実行
      EstimateException.NoMatchMaintenanceGuideException exception = assertThrows(
          EstimateException.NoMatchMaintenanceGuideException.class,
          () -> estimateService.registerEstimateBase(1));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new NoMatchMaintenanceGuideException().getMessage());
    }

    @Test
    void 既にオイルが登録されている状態でさらにオイルを登録すると重複エラーをスローする() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(1) // オイル商品ID
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilMock().get(0);
      GuideProductPermission guideProductPermission = estimateTestHelper.guideProductPermissionMock();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(true).when(estimateRepository)
          .existOilProductsByEstimateBaseId(estimateBase.getEstimateBaseId());

      // 実行
      EstimateException.ExistOilProductsException exception = assertThrows(
          ExistOilProductsException.class,
          () -> estimateService.registerEstimateProduct(1, estimateProductCreateRequest));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new ExistOilProductsException().getMessage());
    }

    @Test
    void 既にオイルフィルターが登録されている状態でオイルフィルターを登録しようとすると重複エラーをスローする() {
      // 準備
      EstimateProductCreateRequest estimateProductCreateRequest = EstimateProductCreateRequest.builder()
          .productId(11) // オイルフィルター商品ID
          .quantity(null)
          .build();

      EstimateBase estimateBase = estimateTestHelper.estimateBaseMock();
      Product product = masterTestHelper.productOilFilterMock().get(0);
      GuideProductPermission guideProductPermission = estimateTestHelper.guideProductPermissionMock();
      doReturnEstimateProductRegisterStub(estimateBase, product, guideProductPermission);
      doReturn(true).when(estimateRepository)
          .existOilFilterProductsByEstimateBaseId(estimateBase.getEstimateBaseId());

      // 実行
      ExistOilFilterProductsException exception = assertThrows(
          ExistOilFilterProductsException.class,
          () -> estimateService.registerEstimateProduct(1, estimateProductCreateRequest));
      // 検証
      assertThat(exception.getMessage()).isEqualTo(
          new ExistOilFilterProductsException().getMessage());
    }

  }

  @Nested
  class DeleteClass {

    @Test
    void 見積もりベースを削除できる() {
      // 準備
      estimateService.deleteEstimateBase(1);
      // 検証
      verify(estimateRepository).deleteEstimateBaseById(1);
    }

    @Test
    void 見積もり商品を削除できる() {
      // 準備
      EstimateProduct estimateProduct = estimateTestHelper.estimateProductsMock().get(0);
      // 実行
      estimateService.deleteEstimateProduct(estimateProduct.getEstimateProductId());

      // 検証
      verify(estimateRepository).deleteEstimateProductById(estimateProduct.getEstimateProductId());
    }
  }

  @Nested
  class UpdateClass {

    @Test
    void 見積もり商品を更新できる() {
      //　準備
      EstimateProduct estimateProduct = estimateTestHelper.estimateProductsMock().get(0);
      EstimateProductUpdateRequest estimateProductCreateRequest = EstimateProductUpdateRequest.builder()
          .quantity(5.0)
          .unitPrice(new BigDecimal("1000"))
          .totalPrice(new BigDecimal("5000"))
          .build();

      doReturn(Optional.of(estimateProduct)).when(estimateRepository)
          .findEstimateProductById(estimateProduct.getEstimateProductId());

      // 実行
      estimateService.updateEstimateProduct(estimateProduct.getEstimateProductId(),
          estimateProductCreateRequest);
      // 検証

      ArgumentCaptor<EstimateProduct> captor = ArgumentCaptor.forClass(EstimateProduct.class);
      verify(estimateRepository).updateEstimateProduct(captor.capture());
      EstimateProduct actual = captor.getValue();
      assertThat(actual.getEstimateProductId()).isEqualTo(estimateProduct.getEstimateProductId());
      assertThat(actual.getQuantity()).isEqualTo(5.0);
    }
  }
}
