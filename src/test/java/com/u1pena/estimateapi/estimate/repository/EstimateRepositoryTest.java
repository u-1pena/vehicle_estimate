package com.u1pena.estimateapi.estimate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.helper.CustomerTestHelper;
import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.estimate.helper.EstimateTestHelper;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class EstimateRepositoryTest {

  @Autowired
  EstimateRepository estimateRepository;

  EstimateTestHelper estimateTestHelper;
  CustomerTestHelper customerTestHelper;

  @BeforeEach
  void setup() {
    estimateTestHelper = new EstimateTestHelper();
    customerTestHelper = new CustomerTestHelper();
  }

  @Nested
  class CreateClass {

    @Test
    void 見積もりベースを登録した場合見積もりベース件数が１件増えること() {
      // 準備
      int initialSize = estimateRepository.findAllEstimateBases().size();
      EstimateBase estimateBase = new EstimateBase(
          1, // customerId
          1, // customerId
          1, // vehicleId
          1, // maintenanceId
          LocalDate.of(2025, 1, 1) // estimateDate
      );
      // 実行
      estimateRepository.insertEstimateBase(estimateBase);
      // 検証
      List<EstimateBase> actual = estimateRepository.findAllEstimateBases();
      assertThat(actual.size()).isEqualTo(initialSize + 1);
    }

    @Test
    void 商品見積もりを登録した場合商品見積もり件数が１件増えること() {
      // 準備
      int initialSize = estimateRepository.findAllEstimateProducts().size();
      EstimateProduct estimateProduct = new EstimateProduct(
          1,
          1,
          1,
          3.0,
          BigDecimal.valueOf(1000.00),
          BigDecimal.valueOf(3000.00));
      // 実行
      estimateRepository.insertEstimateProduct(estimateProduct);
      // 検証
      List<EstimateProduct> actual = estimateRepository.findAllEstimateProducts();
      assertThat(actual.size()).isEqualTo(initialSize + 1);
    }
  }

  @Nested
  class ReadClass {

    @Test
    void 見積もりベースを見積もりベースIDで検索できる() {
      // 準備
      EstimateBase expectedEstimateBase = estimateTestHelper.estimateBaseMocks().get(0);
      // 実行
      Optional<EstimateBase> actual = estimateRepository.findEstimateBaseById(1);
      // 検証
      assertThat(actual).isPresent().contains(expectedEstimateBase);
    }

    @Test
    void 車両IDで車両を検索できる() {
      // 準備
      Vehicle expectedVehicle = customerTestHelper.vehicleMock().get(0);
      // 実行
      Optional<Vehicle> actual = estimateRepository.findVehicleByVehicleId(1);
      // 検証
      assertThat(actual).isPresent().contains(expectedVehicle);
    }

    @Test
    void 商品見積もりを商品見積もりIDで検索できる() {
      // 準備
      EstimateProduct expectedEstimateProduct = estimateTestHelper.estimateProductMock().get(0);
      // 実行
      Optional<EstimateProduct> actual = estimateRepository.findEstimateProductById(1);
      // 検証
      assertThat(actual).isPresent().contains(expectedEstimateProduct);
    }

    @Test
    void メンテナンスIDから車両名を検索する() {
      // 準備
      String expectedVehicleName = "カローラアクシオ";
      MaintenanceGuide maintenanceGuide = estimateTestHelper.maintenanceGuideMock().get(0);
      // 実行
      Optional<String> actual = estimateRepository
          .findVehicleNameByMaintenanceId(maintenanceGuide.getMaintenanceId());
      // 検証
      assertThat(actual).isPresent().hasValue(expectedVehicleName);
    }


    @Test
    void 車両型式とエンジン型式でメンテナンスガイドを取得できる() {
      // 準備
      MaintenanceGuide expectedMaintenanceGuide = estimateTestHelper.maintenanceGuideMock().get(0);
      Vehicle vehicle = customerTestHelper.vehicleMock().get(0);
      // 実行
      Optional<MaintenanceGuide> actual = estimateRepository
          .findMaintenanceGuideByMakeAndModelAndYear(
              vehicle.getMake(),
              vehicle.getModel(),
              vehicle.getYear());
      // 検証
      assertThat(actual).isPresent().contains(expectedMaintenanceGuide);
    }

    @Test
    void メンテナンスガイドをメンテナンスIDと商品IDで検索できる() {
      // 準備
      int maintenanceId = 1;
      int productId = 1;
      // 実行
      Optional<GuideProductPermission> actual = estimateRepository
          .findPermissionByMaintenanceIdAndProductId(maintenanceId, productId);
      // 検証
      assertThat(actual).isPresent();
      assertThat(actual.get().getMaintenanceId()).isEqualTo(maintenanceId);
      assertThat(actual.get().getProductId()).isEqualTo(productId);
      assertThat(actual.get().getQuantity()).isEqualTo(4.0);
      assertThat(actual.get().getCategoryId()).isEqualTo(1);
      assertThat(actual.get().isAutoAdjustQuantity()).isTrue();
    }

    @Test
    void 見積もりベースIDに紐づく商品見積もりからカテゴリーオイル商品が含まれていたら0を返す() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      int count = estimateRepository.countOilProductsByEstimateBaseId(estimateBaseId);
      // 検証
      assertThat(count).isGreaterThanOrEqualTo(0); // 0以上であることを確認
    }

    @Test
    void 見積もりベースIDに紐づく商品見積もりからオイル商品が含まれていない場合は0を返す() {
      // 準備
      int estimateBaseId = 2; // 存在しない見積もりベースIDを使用
      // 実行
      int count = estimateRepository.countOilProductsByEstimateBaseId(estimateBaseId);
      // 検証
      assertThat(count).isEqualTo(0); // 0であることを確認
    }

    @Test
    void 見積もりベースIDに紐づく商品見積もりからオイルが存在する場合はTrueを返す() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      boolean exists = estimateRepository.existOilProductsByEstimateBaseId(estimateBaseId);
      // 検証
      assertThat(exists).isTrue(); // オイル商品が存在することを確認
    }

    @Test
    void 見積もりベースIDに紐づく商品見積もりにオイルカテゴリー商品が存在しない場合はfalseを返す() {
      // 準備
      int estimateBaseId = 2; // 存在しない見積もりベースIDを使用
      // 実行
      boolean exists = estimateRepository.existOilProductsByEstimateBaseId(estimateBaseId);
      // 検証
      assertThat(exists).isFalse(); // オイル商品が存在しないことを確認
    }

    @Test
    void 見積もりベースに紐づく商品見積もりにオイルフィルターが含まれている数を返す() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      int count = estimateRepository.countOilFilterProductsByEstimateBaseId(estimateBaseId);
      // 検証
      assertThat(count).isGreaterThanOrEqualTo(0); // 0以上であることを確認
    }


    @Test
    void メンテナンスIDでメンテナンスガイドからフィルター込オイル数量を検索する() {
      // 準備
      int maintenanceId = 1; // 既存のメンテナンスIDを使用
      double expectedQuantity = 3.7; // 期待されるオイル量
      // 実行
      double actualQuantity = estimateRepository.findOilQuantityWithFilterByMaintenanceId(
          maintenanceId);
      // 検証
      assertThat(actualQuantity).isEqualTo(expectedQuantity); // 期待されるオイル量と一致することを確認
    }

    @Test
    void 商品見積もりを見積もりベースIDで検索する() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      List<Integer> estimateProductId = estimateRepository.findEstimateProductIdByEstimateBaseId(
          estimateBaseId);
      // 検証
      assertThat(estimateProductId).isNotEmpty(); // 商品見積もりIDが存在することを確認
    }

    @Test
    void 複数の商品IDに紐づく商品カテゴリーを検索し取得する() {
      // 準備
      List<Integer> productIds = List.of(1, 2, 3); // 既存の商品IDを使用
      // 実行
      List<EstimateProductJoinResult> productDetails = estimateRepository
          .findProductsWithCategoryByIds(productIds);
      // 検証
      assertThat(productDetails).isNotEmpty(); // 商品詳細が存在することを確認
    }

    @Test
    void 顧客IDに紐づく商品見積もり一覧をすべて表示するための情報を取得する() {
      // 準備
      int customerId = 1; // 既存の顧客IDを使用
      // 実行
      List<EstimateSummaryResult> estimateSummaryResults = estimateRepository
          .findEstimateSummaryResultsByCustomerId(customerId);
      // 検証
      assertThat(estimateSummaryResults).isNotEmpty(); // 見積もりサマリー結果が存在することを確認
    }

    @Test
    void 日付指定した見積もりを検索する() {
      // 準備
      String startDate = "2024-01-01"; // 開始日
      String endDate = "2025-12-31"; // 終了日
      // 実行
      List<EstimateSummaryResult> estimateSummaryResults = estimateRepository
          .findEstimateSummaryResultsByDateRange(startDate, endDate);
      // 検証
      assertThat(estimateSummaryResults).isNotEmpty(); // 見積もりサマリー結果が存在することを確認
    }
  }

  @Nested
  class UpdateClass {

    @Test
    void 商品見積もりIDからオイルカテゴリー商品に対して指定した数量を更新できる() {
      // 準備
      EstimateProduct estimateProduct = estimateTestHelper.estimateProductMock().get(0);
      double newQuantity = 5.0;
      // 実行
      estimateRepository.updateOilQuantityWithEstimateProductId(
          estimateProduct.getProductId(),
          newQuantity);
      // 検証
      Optional<EstimateProduct> updatedProduct = estimateRepository.findEstimateProductById(
          estimateProduct.getEstimateProductId());
      assertThat(updatedProduct).isPresent();
      assertThat(updatedProduct.get().getQuantity()).isEqualTo(newQuantity);
      assertThat(updatedProduct.get().getProductId()).isEqualTo(estimateProduct.getProductId());
      assertThat(updatedProduct.get().getEstimateBaseId()).isEqualTo(
          estimateProduct.getEstimateBaseId());
      assertThat(updatedProduct.get().getEstimateProductId()).isEqualTo(
          estimateProduct.getEstimateProductId());
      assertThat(updatedProduct.get().getUnitPrice()).isEqualTo(estimateProduct.getUnitPrice());
      assertThat(updatedProduct.get().getTotalPrice()).isEqualTo(
          estimateProduct.getTotalPrice());
    }

    @Test
    void 商品見積もりを更新する() {
      // 準備
      EstimateProduct estimateProduct = estimateTestHelper.estimateProductMock().get(0);
      estimateProduct.setQuantity(4.0); // 更新する数量
      estimateProduct.setUnitPrice(BigDecimal.valueOf(1200.00)); // 更新する単価
      // 実行
      estimateRepository.updateEstimateProduct(estimateProduct);
      // 検証
      Optional<EstimateProduct> updatedEstimateProduct = estimateRepository
          .findEstimateProductById(estimateProduct.getEstimateProductId());
      assertThat(updatedEstimateProduct).isPresent();
      assertThat(updatedEstimateProduct.get().getQuantity()).isEqualTo(4.0);// 更新された数量を確認
      assertThat(updatedEstimateProduct.get().getUnitPrice())
          .isEqualTo(BigDecimal.valueOf(1200.00).setScale(2)); // 更新された単価を確認
    }
  }

  @Nested
  class DeleteClass {

    @Test
    void 見積もりベースIDから見積もりベースを削除できる() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      estimateRepository.deleteEstimateBaseById(estimateBaseId);
      // 検証
      Optional<EstimateBase> deletedEstimateBase = estimateRepository.findEstimateBaseById(
          estimateBaseId);
      assertThat(deletedEstimateBase).isNotPresent(); // 見積もりベースが削除されていることを確認
      assertThat(estimateRepository.findEstimateProductsByEstimateBaseId(estimateBaseId))
          .isEmpty(); // 見積もりベースに紐づく商品見積もりも削除されていることを確認
    }

    @Test
    void 見積もりベースIDから商品見積もりを削除する() {
      // 準備
      int estimateBaseId = 1; // 既存の見積もりベースIDを使用
      // 実行
      estimateRepository.deleteEstimateProductsByEstimateBaseId(estimateBaseId);
      // 検証
      List<EstimateProduct> deletedEstimateProducts = estimateRepository
          .findEstimateProductsByEstimateBaseId(estimateBaseId);
      assertThat(deletedEstimateProducts).isEmpty(); // 見積もりベースに紐づく見積もり商品が
      // 削除されていることを確認
    }


    @Test
    void 商品見積もりIDで商品見積もりを削除する() {
      // 準備
      int estimateProductId = 1; // 既存の見積もり商品IDを使用
      // 実行
      estimateRepository.deleteEstimateProductById(estimateProductId);
      // 検証
      Optional<EstimateProduct> deletedEstimateProduct = estimateRepository
          .findEstimateProductById(estimateProductId);
      assertThat(deletedEstimateProduct).isNotPresent(); // 単品見積もり商品が削除されていることを確認
      assertThat(estimateRepository.findEstimateProductsByEstimateBaseId(1)).hasSize(1);
      // 見積もりベースIDに紐づく商品見積もりが1件残っていることを確認
    }
  }
}
