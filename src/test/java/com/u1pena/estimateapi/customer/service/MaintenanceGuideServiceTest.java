package com.u1pena.estimateapi.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.customer.helper.MasterTestHelper;
import com.u1pena.estimateapi.estimate.dto.GuideProductPermissionCreateContext;
import com.u1pena.estimateapi.master.converter.MaintenanceGuideCreateConverter;
import com.u1pena.estimateapi.master.dto.request.MaintenanceGuideCreateRequest;
import com.u1pena.estimateapi.master.dto.request.ProductCategoryCreateRequest;
import com.u1pena.estimateapi.master.dto.request.ProductCreateRequest;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.entity.ProductCategory;
import com.u1pena.estimateapi.master.exception.MasterException.MaintenanceGuideAlreadyExistsException;
import com.u1pena.estimateapi.master.repository.MasterRepository;
import com.u1pena.estimateapi.master.service.MaintenanceGuideService;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaintenanceGuideServiceTest {

  @Mock
  MasterRepository masterRepository;
  MaintenanceGuideCreateConverter maintenanceCreateBaseCreateConverter;

  @InjectMocks
  MaintenanceGuideService maintenanceGuideService;

  MasterTestHelper masterTestHelper;

  @BeforeEach
  void setup() {
    masterTestHelper = new MasterTestHelper();
  }

  @Nested
  class CreateClass {

    @Test
    void メンテナンスガイドを登録できること() {
      // Arrange
      MaintenanceGuideCreateRequest maintenanceGuideCreateRequest = masterTestHelper.maintenanceGuideCreateRequestMock();
      MaintenanceGuide maintenanceGuide = new MaintenanceGuide();
      maintenanceGuide.setMake(maintenanceGuideCreateRequest.getMake());
      maintenanceGuide.setVehicleName(maintenanceGuideCreateRequest.getVehicleName());
      maintenanceGuide.setModel(maintenanceGuideCreateRequest.getModel());
      maintenanceGuide.setType(maintenanceGuideCreateRequest.getType());
      maintenanceGuide.setStartYear(YearMonth.parse(maintenanceGuideCreateRequest.getStartYear()));
      maintenanceGuide.setEndYear(YearMonth.parse(maintenanceGuideCreateRequest.getEndYear()));
      maintenanceGuide.setOilViscosity(maintenanceGuideCreateRequest.getOilViscosity());
      maintenanceGuide.setOilQuantityWithFilter(
          maintenanceGuideCreateRequest.getOilQuantityWithFilter());
      maintenanceGuide.setOilQuantityWithoutFilter(
          maintenanceGuideCreateRequest.getOilQuantityWithoutFilter());
      maintenanceGuide.setOilFilterPartNumber(
          maintenanceGuideCreateRequest.getOilFilterPartNumber());
      maintenanceGuide.setCarWashSize(
          CarWashSize.valueOf(maintenanceGuideCreateRequest.getCarWashSize()));
      Product productOil = masterTestHelper.productOilMock().get(0);
      Product productWash = masterTestHelper.productCarWashMock().get(3);
      Product productOilFilter = masterTestHelper.productOilFilterMock().get(0);

      // Mock the repository behavior
      doNothing().when(masterRepository).createMaintenanceGuide(any());
      doNothing().when(masterRepository)
          .createGuideProductPermission(any(GuideProductPermissionCreateContext.class));
      when(masterRepository.findProductByOilViscosity
          (maintenanceGuide.getOilViscosity())).thenReturn(List.of(productOil));
      when(masterRepository.findProductByCarWashSize(
          maintenanceGuide.getCarWashSize())).thenReturn(List.of(productWash));
      when(masterRepository.findProductByOilFilterPartNumber(
          maintenanceGuide.getOilFilterPartNumber())).thenReturn(List.of(productOilFilter));

      // Act
      MaintenanceGuide actual = maintenanceGuideService.registerMaintenanceGuide(
          maintenanceGuideCreateRequest);

      // Assert
      assertThat(actual.getMake()).isEqualTo(maintenanceGuideCreateRequest.getMake());
      assertThat(actual.getVehicleName()).isEqualTo(maintenanceGuideCreateRequest.getVehicleName());
      assertThat(actual.getModel()).isEqualTo(maintenanceGuideCreateRequest.getModel());
      assertThat(actual.getType()).isEqualTo(maintenanceGuideCreateRequest.getType());
      assertThat(actual.getStartYear()).isEqualTo(
          YearMonth.parse(maintenanceGuideCreateRequest.getStartYear()));
      assertThat(actual.getEndYear()).isEqualTo(
          YearMonth.parse(maintenanceGuideCreateRequest.getEndYear()));
      assertThat(actual.getOilViscosity()).isEqualTo(
          maintenanceGuideCreateRequest.getOilViscosity());
      assertThat(actual.getOilQuantityWithFilter())
          .isEqualTo(maintenanceGuideCreateRequest.getOilQuantityWithFilter());
      assertThat(actual.getOilQuantityWithoutFilter())
          .isEqualTo(maintenanceGuideCreateRequest.getOilQuantityWithoutFilter());
      assertThat(actual.getOilFilterPartNumber())
          .isEqualTo(maintenanceGuideCreateRequest.getOilFilterPartNumber());
      assertThat(actual.getCarWashSize()).isEqualTo(
          CarWashSize.valueOf(maintenanceGuideCreateRequest.getCarWashSize()));

      verify(masterRepository, times(3)).
          createGuideProductPermission(any(GuideProductPermissionCreateContext.class));

    }


    @Test
    void 商品カテゴリーを登録できること() {
      //Arrange
      ProductCategoryCreateRequest productCategoryCreateRequest = new ProductCategoryCreateRequest();
      productCategoryCreateRequest.setCategoryName("エンジンオイル");

      //Mock the repository behavior
      doNothing().when(masterRepository).createProductCategory(any());

      //Act
      ProductCategory actual = maintenanceGuideService.registerProductCategory(
          productCategoryCreateRequest);

      assertThat(actual.getCategoryName()).isEqualTo("エンジンオイル");
      verify(masterRepository, times(1))
          .createProductCategory(any(ProductCategory.class));
    }

    @Test
    void 商品を登録できること() {
      // Arrange
      ProductCreateRequest productCreateRequest = new ProductCreateRequest();
      productCreateRequest.setCategoryId(1);
      productCreateRequest.setProductName("ハイグレードオイル_0w-20");
      productCreateRequest.setDescription("化学合成油_0w-20");
      productCreateRequest.setGuideMatchKey("0w-20");
      productCreateRequest.setPrice(BigDecimal.valueOf(2800.0));
      doNothing().when(masterRepository).createProduct(any());

      // Act
      Product actual = maintenanceGuideService.registerProduct(productCreateRequest);

      // Assert
      assertThat(actual.getCategoryId()).isEqualTo(productCreateRequest.getCategoryId());
      assertThat(actual.getProductName()).isEqualTo(productCreateRequest.getProductName());
      assertThat(actual.getDescription()).isEqualTo(productCreateRequest.getDescription());
      assertThat(actual.getPrice()).isEqualTo(productCreateRequest.getPrice());
      verify(masterRepository, times(1)).createProduct(any(Product.class));
    }

    @Test
    void 使用できる商品が２種類あっても登録できること() {
      // Arrange
      MaintenanceGuideCreateRequest request = masterTestHelper.maintenanceGuideCreateRequestMock();
      MaintenanceGuide maintenanceGuide = new MaintenanceGuide();
      maintenanceGuide.setMake(request.getMake());
      maintenanceGuide.setVehicleName(request.getVehicleName());
      maintenanceGuide.setModel(request.getModel());
      maintenanceGuide.setType(request.getType());
      maintenanceGuide.setStartYear(YearMonth.parse(request.getStartYear()));
      maintenanceGuide.setEndYear(YearMonth.parse(request.getEndYear()));
      maintenanceGuide.setOilViscosity(request.getOilViscosity());
      maintenanceGuide.setOilQuantityWithFilter(request.getOilQuantityWithFilter());
      maintenanceGuide.setOilQuantityWithoutFilter(request.getOilQuantityWithoutFilter());
      maintenanceGuide.setOilFilterPartNumber(request.getOilFilterPartNumber());
      maintenanceGuide.setCarWashSize(CarWashSize.valueOf(request.getCarWashSize()));

      Product oilProduct1 = new Product();
      oilProduct1.setCategoryId(1);
      oilProduct1.setProductId(1);
      oilProduct1.setProductName("ハイグレードオイル_0w-20");
      oilProduct1.setDescription("化学合成油_0w-20");
      oilProduct1.setGuideMatchKey("0w-20");
      oilProduct1.setPrice(BigDecimal.valueOf(2800.0));

      Product oilProduct2 = new Product();
      oilProduct2.setCategoryId(1);
      oilProduct2.setProductId(2);
      oilProduct2.setProductName("ノーマルオイル_0w-20");
      oilProduct2.setDescription("鉱物油_0w-20");
      oilProduct2.setGuideMatchKey("0w-20");
      oilProduct2.setPrice(BigDecimal.valueOf(2000.0));

      // Mock the repository behavior
      doNothing().when(masterRepository).createMaintenanceGuide(any());
      when(masterRepository.findProductByOilViscosity(
          maintenanceGuide.getOilViscosity())).thenReturn(List.of(oilProduct1, oilProduct2));

      // Act
      MaintenanceGuide actual = maintenanceGuideService.registerMaintenanceGuide(request);

      // Assert
      assertThat(actual.getMake()).isEqualTo(request.getMake());
      assertThat(actual.getVehicleName()).isEqualTo(request.getVehicleName());
      assertThat(actual.getModel()).isEqualTo(request.getModel());
      assertThat(actual.getType()).isEqualTo(request.getType());
      assertThat(actual.getStartYear()).isEqualTo(YearMonth.parse(request.getStartYear()));
      assertThat(actual.getEndYear()).isEqualTo(YearMonth.parse(request.getEndYear()));
      assertThat(actual.getOilViscosity()).isEqualTo(request.getOilViscosity());
      assertThat(actual.getOilQuantityWithFilter())
          .isEqualTo(request.getOilQuantityWithFilter());
      assertThat(actual.getOilQuantityWithoutFilter())
          .isEqualTo(request.getOilQuantityWithoutFilter());
      assertThat(actual.getOilFilterPartNumber())
          .isEqualTo(request.getOilFilterPartNumber());
      assertThat(actual.getCarWashSize()).isEqualTo(CarWashSize.valueOf(request.getCarWashSize()));
      verify(masterRepository, times(2))
          .createGuideProductPermission(any(GuideProductPermissionCreateContext.class));
    }

    @Test
    void 商品が存在しない場合は商品権限を作成しないこと() {
      // Arrange
      MaintenanceGuideCreateRequest request = masterTestHelper.maintenanceGuideCreateRequestMock();
      MaintenanceGuide maintenanceGuide = new MaintenanceGuide();
      maintenanceGuide.setMake(request.getMake());
      maintenanceGuide.setVehicleName(request.getVehicleName());
      maintenanceGuide.setModel(request.getModel());
      maintenanceGuide.setType(request.getType());
      maintenanceGuide.setStartYear(YearMonth.parse(request.getStartYear()));
      maintenanceGuide.setEndYear(YearMonth.parse(request.getEndYear()));
      maintenanceGuide.setOilViscosity(request.getOilViscosity());
      maintenanceGuide.setOilQuantityWithFilter(request.getOilQuantityWithFilter());
      maintenanceGuide.setOilQuantityWithoutFilter(request.getOilQuantityWithoutFilter());
      maintenanceGuide.setOilFilterPartNumber(request.getOilFilterPartNumber());
      maintenanceGuide.setCarWashSize(CarWashSize.valueOf(request.getCarWashSize()));

      // Mock the repository behavior
      doNothing().when(masterRepository).createMaintenanceGuide(any());
      when(masterRepository.findProductByOilViscosity(
          maintenanceGuide.getOilViscosity())).thenReturn(List.of());

      // Act
      MaintenanceGuide actual = maintenanceGuideService.registerMaintenanceGuide(request);

      // Assert
      assertThat(actual.getMake()).isEqualTo(request.getMake());
      assertThat(actual.getVehicleName()).isEqualTo(request.getVehicleName());
      assertThat(actual.getModel()).isEqualTo(request.getModel());
      assertThat(actual.getType()).isEqualTo(request.getType());
      assertThat(actual.getStartYear()).isEqualTo(YearMonth.parse(request.getStartYear()));
      assertThat(actual.getEndYear()).isEqualTo(YearMonth.parse(request.getEndYear()));
      assertThat(actual.getOilViscosity()).isEqualTo(request.getOilViscosity());
      assertThat(actual.getOilQuantityWithFilter())
          .isEqualTo(request.getOilQuantityWithFilter());
      assertThat(actual.getOilQuantityWithoutFilter())
          .isEqualTo(request.getOilQuantityWithoutFilter());
      assertThat(actual.getOilFilterPartNumber())
          .isEqualTo(request.getOilFilterPartNumber());
      assertThat(actual.getCarWashSize()).isEqualTo(CarWashSize.valueOf(request.getCarWashSize()));
      verify(masterRepository, times(0))
          .createGuideProductPermission(any(GuideProductPermissionCreateContext.class));
    }

    @Test
    void 既にメンテナンスガイドが登録されている場合例外処理が行われること() {
      // Arrange
      MaintenanceGuide existingGuide = masterTestHelper.maintenanceGuideMock().get(0);
      MaintenanceGuideCreateRequest request = masterTestHelper.maintenanceGuideCreateRequestMock();

      // Mock the repository behavior
      when(masterRepository.findMaintenanceGuideByMakeAndModelAndTypeAndYear(
          any(MaintenanceGuide.class))).thenReturn(java.util.Optional.of(existingGuide));

      // Act
      MaintenanceGuideAlreadyExistsException exception = assertThrows(
          MaintenanceGuideAlreadyExistsException.class,
          () -> maintenanceGuideService.registerMaintenanceGuide(request));

      // Assert
      assertThat(exception.getMessage()).isEqualTo("Maintenance guide already exists");
    }
  }
}
