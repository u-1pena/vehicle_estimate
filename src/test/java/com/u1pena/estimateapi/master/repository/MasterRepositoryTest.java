package com.u1pena.estimateapi.master.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.estimate.dto.GuideProductPermissionCreateContext;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.entity.ProductCategory;
import com.u1pena.estimateapi.master.helper.MasterTestHelper;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class MasterRepositoryTest {

  @Autowired
  MasterRepository masterRepository;

  MasterTestHelper masterTestHelper;

  @BeforeEach
  void setup() {
    masterTestHelper = new MasterTestHelper();
  }

  @Nested
  class CreateClass {

    @Test
    void メンテナンスガイドを作成することができる() {
      int initialSize = masterRepository.findAllMaintenanceGuides().size();
      MaintenanceGuide maintenanceGuide = masterTestHelper.maintenanceGuideMock().get(0);
      masterRepository.createMaintenanceGuide(maintenanceGuide);

      List<MaintenanceGuide> actual = masterRepository.findAllMaintenanceGuides();
      assertThat(actual).hasSize(initialSize + 1);
    }
  }

  @Test
  void 商品カテゴリーを作成することができる() {
    int initialSize = masterRepository.findAllProductCategories().size();
    ProductCategory productCategory = new ProductCategory();
    productCategory.setCategoryName("Test Category");
    masterRepository.createProductCategory(productCategory);

    List<ProductCategory> actual = masterRepository.findAllProductCategories();
    assertThat(actual).hasSize(initialSize + 1);
  }

  @Test
  void 商品を作成することができる() {
    int initialSize = masterRepository.findAllProducts().size();
    Product product = new Product();
    product.setProductName("Test Product");
    product.setCategoryId(1);
    product.setDescription("This is a test product.");
    product.setGuideMatchKey("TEST-1234");
    product.setPrice(BigDecimal.valueOf(1000));
    masterRepository.createProduct(product);
    List<Product> actual = masterRepository.findAllProducts();
    assertThat(actual).hasSize(initialSize + 1);
  }

  @Test
  void 商品権限を作成することができる() {
    int initialSize = masterRepository.findAllGuideProductPermissions().size();
    GuideProductPermissionCreateContext context = GuideProductPermissionCreateContext.builder()
        .maintenanceId(2)
        .productId(1)
        .categoryId(1)
        .quantity(3.4)
        .autoAdjustQuantity(true)
        .build();
    masterRepository.createGuideProductPermission(context);

    List<GuideProductPermission> actual = masterRepository.findAllGuideProductPermissions();
    assertThat(actual).hasSize(initialSize + 1);
  }

  @Nested
  class readClass {

    @Test
    void メンテナンスガイドをメーカー_モデル_タイプ_年式で検索できる() {
      MaintenanceGuide maintenanceGuide = new MaintenanceGuide();
      maintenanceGuide.setMake("toyota");
      maintenanceGuide.setModel("DBA-NZE141");
      maintenanceGuide.setType("1NZ");
      maintenanceGuide.setStartYear(YearMonth.parse("2006-10"));
      maintenanceGuide.setEndYear(YearMonth.parse("2012-05"));

      Optional<MaintenanceGuide> actual = masterRepository
          .findMaintenanceGuideByMakeAndModelAndTypeAndYear(maintenanceGuide);

      assertThat(actual).isPresent();
      assertThat(actual.get().getMake()).isEqualTo(maintenanceGuide.getMake());
      assertThat(actual.get().getModel()).isEqualTo(maintenanceGuide.getModel());
      assertThat(actual.get().getType()).isEqualTo(maintenanceGuide.getType());
      assertThat(actual.get().getStartYear()).isEqualTo(maintenanceGuide.getStartYear());
      assertThat(actual.get().getEndYear()).isEqualTo(maintenanceGuide.getEndYear());
    }

    @Test
    void 指定したオイル粘度でオイルを検索できる() {
      String oilViscosity = "5w-30";
      List<Product> actual = masterRepository.findProductByOilViscosity(oilViscosity);
      assertThat(actual).isNotEmpty();
      assertThat(actual.get(0).getGuideMatchKey()).isEqualTo(oilViscosity);
    }

    @Test
    void 指定した洗車サイズで洗車商品を検索できる() {
      CarWashSize carWashSize = CarWashSize.S;
      List<Product> actual = masterRepository.findProductByCarWashSize(carWashSize);
      assertThat(actual).isNotEmpty();
      assertThat(actual.get(0).getGuideMatchKey()).isEqualTo(carWashSize.name());
    }

    @Test
    void 指定したオイルフィルター品番でオイルフィルター商品を検索できる() {
      String oilFilterPartNumber = "90915-10003";
      List<Product> actual = masterRepository.findProductByOilFilterPartNumber(
          oilFilterPartNumber);
      assertThat(actual).isNotEmpty();
      assertThat(actual.get(0).getGuideMatchKey()).isEqualTo(oilFilterPartNumber);
    }
  }
}
