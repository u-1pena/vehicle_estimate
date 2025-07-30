package com.u1pena.estimateapi.estimate.service;

import static com.u1pena.estimateapi.common.CategoryConstants.CATEGORY_OIL;
import static com.u1pena.estimateapi.common.CategoryConstants.CATEGORY_OIL_FILTER;
import static org.thymeleaf.util.ListUtils.isEmpty;

import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.customer.exception.CustomerAddressException;
import com.u1pena.estimateapi.customer.exception.CustomerException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleNotFoundException;
import com.u1pena.estimateapi.customer.service.CustomerService;
import com.u1pena.estimateapi.estimate.converter.EstimateBaseCreateConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateCustomerAddressConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateCustomerConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateFullConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateHeaderConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateProductCreateConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateProductUpdateConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateProductsConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateSummaryConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateSummaryDateConverter;
import com.u1pena.estimateapi.estimate.converter.EstimateVehicleConverter;
import com.u1pena.estimateapi.estimate.dto.EstimateProductContext;
import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductCreateRequest;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.dto.response.CustomerAddressResponse;
import com.u1pena.estimateapi.estimate.dto.response.CustomerResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateHeaderResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateProductResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;
import com.u1pena.estimateapi.estimate.dto.response.VehicleResponse;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.estimate.exception.EstimateException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.EstimateBaseNotFoundException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.NoMatchMaintenanceGuideException;
import com.u1pena.estimateapi.estimate.repository.EstimateRepository;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.repository.MasterRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EstimateService {


  CustomerService customerService;
  EstimateRepository estimateRepository;
  MasterRepository masterRepository;


  public EstimateService(CustomerService customerService, EstimateRepository estimateRepository,
      MasterRepository masterRepository) {
    this.customerService = customerService;
    this.estimateRepository = estimateRepository;
    this.masterRepository = masterRepository;
  }

  /**
   * 見積もりの詳細情報を取得する。 見積もりIDを指定して、見積もりのヘッダー情報と商品情報を取得し、合計金額を計算して返す。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @return EstimateFullResponse 見積もりの詳細情報を含むレスポンスオブジェクト
   */
  public EstimateFullResponse getEstimateFullById(int estimateBaseId) {
    EstimateHeaderResponse estimateHeaderResponse = getEstimateHeaderById(estimateBaseId);
    List<EstimateProductResponse> estimateProductResponse = getEstimateProductByEstimateId(
        estimateBaseId);
    BigDecimal totalPrice = estimateProductResponse.stream()
        .map(EstimateProductResponse::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return EstimateFullConverter.toDto(estimateHeaderResponse, estimateProductResponse, totalPrice);
  }

  /**
   * EstimateBaseのIDを指定して、見積もりヘッダー情報を取得する。 このメソッドは、見積もりの基本情報（顧客情報、車両情報など）を取得するために使用されます。
   * 見積もりIDが存在しない場合は、EstimateBaseNotFoundExceptionをスローします。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @return EstimateHeaderResponse 見積もりヘッダー情報を含むレスポンスオブジェクト
   */
  private EstimateHeaderResponse getEstimateHeaderById(int estimateBaseId) {
    EstimateBase estimateBase = estimateRepository.findEstimateBaseById(estimateBaseId)
        .orElseThrow(EstimateBaseNotFoundException::new);
    Customer customer = findCustomerById(estimateBase.getCustomerId());
    CustomerAddress customerAddress = findCustomerAddressByCustomerId(customer.getCustomerId());
    Vehicle vehicle = findVehicleById(estimateBase.getVehicleId());
    String vehicleName = getVehicleNameByMaintenanceId(estimateBase.getMaintenanceId());
    CustomerResponse customerResponse = EstimateCustomerConverter.toDto(customer);
    CustomerAddressResponse customerAddressResponse = EstimateCustomerAddressConverter.toDto(
        customerAddress);
    VehicleResponse vehicleDto = EstimateVehicleConverter.toDto(vehicleName, vehicle);
    return EstimateHeaderConverter.toDto(
        estimateBaseId, estimateBase.getEstimateDate(), customerResponse, customerAddressResponse,
        vehicleDto);
  }

  /**
   * 見積もりIDに紐づくEstimateProductを取得する。 見積もりIDを指定して、EstimateProductsから関連する商品情報を取得し、DTOに変換して返す。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @return List<EstimateProductResponse> 見積もり商品のリスト
   */
  private List<EstimateProductResponse> getEstimateProductByEstimateId(int estimateBaseId) {
    // まずEstimateProductsからestimateBaseIdに紐づくEstimateProductを取得
    List<EstimateProduct> estimateProducts = estimateRepository.findEstimateProductsByEstimateBaseId(
        estimateBaseId);

    Map<Integer, EstimateProduct> estimateProductMap = estimateProducts.stream()
        .collect(Collectors.toMap(EstimateProduct::getProductId, ep -> ep));

    List<Integer> productDetailList = estimateProducts.stream()
        .map(EstimateProduct::getProductId)
        .toList();
    if (isEmpty(productDetailList)) {
      return List.of(); // 空のリストを返す
    }
    List<EstimateProductJoinResult> result = estimateRepository.findProductsWithCategoryByIds(
        productDetailList);

    return result.stream()
        .map(joinResult -> {
          EstimateProduct estimateProduct = estimateProductMap.get(joinResult.getProductId());
          return EstimateProductsConverter.toDto(joinResult, estimateProduct);
        })
        .toList();
  }

  /**
   * 見積もりの概要をリストで顧客IDから取得する。
   *
   * @param customerId 顧客ID
   * @return 見積もりの概要リスト
   */
  public List<EstimateSummaryResponse> getEstimateSummaryByCustomerId(int customerId) {
    List<EstimateSummaryResult> results = estimateRepository.findEstimateSummaryResultsByCustomerId(
        customerId);
    return results.stream()
        .map(EstimateSummaryConverter::toDto)
        .toList();
  }

  /**
   * 指定した期間内の見積もりの概要をリストで取得する。
   *
   * @param startDate 開始日
   * @param endDate   終了日
   * @return 見積もりの概要リスト
   */
  public List<EstimateSummaryDateResponse> getEstimatesByDateRange(LocalDate startDate,
      LocalDate endDate) {
    List<EstimateSummaryResult> results = estimateRepository
        .findEstimateSummaryResultsByDateRange(startDate.toString(), endDate.toString());
    return results.stream()
        .map(EstimateSummaryDateConverter::toDto)
        .toList();
  }

  /**
   * EstimateBaseを登録する。
   *
   * @param vehicleId
   */
  public int registerEstimateBase(int vehicleId) {
    Vehicle vehicle = findVehicleById(vehicleId);
    MaintenanceGuide maintenanceGuide = searchMaintenanceGuideMatch(vehicle);
    EstimateBase estimateBase = EstimateBaseCreateConverter
        .toEntity(vehicle.getCustomerId(), vehicleId, maintenanceGuide.getMaintenanceId());
    estimateRepository.insertEstimateBase(estimateBase);
    return estimateBase.getEstimateBaseId();
  }


  /**
   * EstimateProductを登録する。
   *
   * @param estimateBaseId
   * @param estimateProductCreateRequest
   * @throws NoMatchMaintenanceGuideException
   */
  @Transactional
  public void registerEstimateProduct(int estimateBaseId,
      EstimateProductCreateRequest estimateProductCreateRequest) {

    // 見積もりの基本情報を取得
    EstimateBase estimateBase = findEstimateBaseById(estimateBaseId);

    // ガイドに基づく商品があるかチェック（Validation + 値利用）
    GuideProductPermission guideProductPermission = fetchPermissionWithValidation(
        estimateBase.getMaintenanceId(), estimateProductCreateRequest.getProductId());

    // 商品を取得
    Product product = findProduct(estimateProductCreateRequest.getProductId());

    // カテゴリーごとの処理
    Double userQuantity = estimateProductCreateRequest.getQuantity();
    double resolvedQuantity = (userQuantity != null)
        ? userQuantity
        : resolveQuantityByCategory(
            product.getCategoryId(),
            estimateBase.getEstimateBaseId(),
            estimateBase.getMaintenanceId(),
            guideProductPermission,
            null);

    // 数量をセット
    estimateProductCreateRequest.setQuantity(resolvedQuantity);

    // エンティティ生成と登録
    EstimateProduct estimateProduct = createEstimateProduct(
        product.getProductId(), estimateBaseId, estimateProductCreateRequest);
    estimateRepository.insertEstimateProduct(estimateProduct);
  }

  /**
   * 数量をカテゴリーごとに解決する。オイルやオイルフィルターなどの特定のカテゴリーに基づいて数量を決定します。
   *
   * @param categoryId             カテゴリーID
   * @param estimateBaseId         見積もりの基本情報を取得するためのID
   * @param maintenanceId          メンテナンスID
   * @param guideProductPermission ガイド商品許可
   * @return 解決された数量
   */
  protected double resolveQuantityByCategory(int categoryId, int estimateBaseId,
      int maintenanceId, GuideProductPermission guideProductPermission, Double userQuantity) {

    if (categoryId == CATEGORY_OIL) {
      validateOilConstraints(estimateBaseId);
      return resolveQuantityForOil(estimateBaseId, maintenanceId, guideProductPermission,
          userQuantity);
    }

    if (categoryId == CATEGORY_OIL_FILTER) {
      handleOilFilterRegistration(estimateBaseId, maintenanceId);
      return resolveQuantityFromUserOrGuide(userQuantity, guideProductPermission);
    }

    return resolveQuantityFromUserOrGuide(userQuantity, guideProductPermission);
  }

  /**
   * オイルの数量を解決する。ユーザーが指定した数量がある場合はそれを使用し、なければガイドから取得します。 オイルフィルターが存在する場合は、フィルター込の数量を使用します。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @param maintenanceId  メンテナンスID
   * @param permission     ガイド商品許可
   * @param userQuantity   ユーザーが指定した数量（nullの場合はガイドから取得）
   * @return 解決されたオイルの数量
   */
  private double resolveQuantityForOil(int estimateBaseId, int maintenanceId,
      GuideProductPermission permission, Double userQuantity) {

    if (userQuantity != null) {
      return resolveQuantityWithValue(userQuantity, permission);
    }

    if (estimateRepository.existOilFilterProductsByEstimateBaseId(estimateBaseId)) {
      double oilWithFilter = estimateRepository.findOilQuantityWithFilterByMaintenanceId(
          maintenanceId);
      return resolveQuantityWithValue(oilWithFilter, permission);
    }

    return resolveQuantityFromGuide(permission);
  }

  /**
   * ユーザーが指定した数量がある場合はそれを使用し、なければガイドから取得します。 ガイドの許可に基づいて数量を解決します。
   *
   * @param userQuantity ユーザーが指定した数量（nullの場合はガイドから取得）
   * @param permission   ガイド商品許可
   * @return 解決された数量
   */
  private double resolveQuantityFromUserOrGuide(Double userQuantity,
      GuideProductPermission permission) {
    if (userQuantity != null) {
      return resolveQuantityWithValue(userQuantity, permission);
    }
    return resolveQuantityFromGuide(permission);
  }

  /**
   * ガイドから数量を解決する。ガイドの許可に基づいて数量を取得します。 自動調整フラグがtrueの場合は、ガイドからの数量を使用し、falseの場合は1.0を返します。
   *
   * @param permission ガイド商品許可
   * @return 解決された数量
   */
  private double resolveQuantityFromGuide(GuideProductPermission permission) {
    return permission.isAutoAdjustQuantity()
        ? permission.getQuantity()
        : 1.0;
  }

  private double resolveQuantityWithValue(double quantity, GuideProductPermission permission) {
    return permission.isAutoAdjustQuantity()
        ? quantity
        : 1.0;
  }

  /**
   * オイルフィルター登録時の処理を行う。このメソッドは、オイルフィルターが存在するかどうかをチェックし、
   * 既に存在する場合は例外をスローします。また、オイルが既に登録されている場合は、オイルの数量をフィルター込の数量に更新します。
   *
   * @param estimateBaseId
   * @param maintenanceId
   * @throws NoMatchMaintenanceGuideException
   */
  private void handleOilFilterRegistration(int estimateBaseId, int maintenanceId) {
    validateOilFilterConstraints(estimateBaseId);

    boolean existOil = estimateRepository.existOilProductsByEstimateBaseId(estimateBaseId);
    if (existOil) {
      int oilProductId = estimateRepository.findProductsWithOilCategoryByEstimateBaseId(
          estimateBaseId);
      int updateEstimateProductId = estimateRepository
          .findEstimateProductIdByEstimateBaseIdAndProductId(estimateBaseId, oilProductId);
      double oilQuantityWithFilter = estimateRepository
          .findOilQuantityWithFilterByMaintenanceId(maintenanceId);
      estimateRepository.updateOilQuantityWithEstimateProductId(updateEstimateProductId,
          oilQuantityWithFilter);
    }
  }


  private void validateOilConstraints(int estimateBaseId) {
    if (estimateRepository.existOilProductsByEstimateBaseId(estimateBaseId)) {
      throw new EstimateException.ExistOilProductsException();
    }
  }

  private void validateOilFilterConstraints(int estimateBaseId) {
    if (estimateRepository.existOilFilterProductsByEstimateBaseId(estimateBaseId)) {
      throw new EstimateException.ExistOilFilterProductsException();
    }
  }

  /**
   * EstimateProductを作成する。
   *
   * @param productId
   * @param estimateBaseId
   * @param estimateProductCreateRequest
   * @return EstimateProduct 作成されたEstimateProductエンティティ
   */
  private EstimateProduct createEstimateProduct(int productId, int estimateBaseId,
      EstimateProductCreateRequest estimateProductCreateRequest) {

    Product product = findProduct(productId);

    BigDecimal unitPrice = product.getPrice();
    double quantity = estimateProductCreateRequest.getQuantity();
    BigDecimal totalPrice = calculateTotalPrice(unitPrice, quantity);

    return EstimateProductCreateConverter.toEntity(
        EstimateProductContext.builder()
            .estimateBaseId(estimateBaseId)
            .product(product)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .totalPrice(totalPrice)
            .build()
    );
  }

  /**
   * 合計金額を計算する。
   *
   * @param unitPrice 単価
   * @param quantity  数量
   * @return 合計金額
   */
  private BigDecimal calculateTotalPrice(BigDecimal unitPrice, double quantity) {
    return unitPrice.multiply(BigDecimal.valueOf(quantity))
        .setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * 車両IDを指定して、車両情報を取得する。
   *
   * @param vehicleId 車両ID
   * @return Vehicle 車両情報
   * @throws VehicleNotFoundException 車両が見つからない場合にスローされる例外
   */
  private Vehicle findVehicleById(int vehicleId) {
    return estimateRepository.findVehicleByVehicleId(vehicleId)
        .orElseThrow(VehicleNotFoundException::new);
  }

  /**
   * 見積もりの基本情報をIDで取得する。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @return EstimateBase 見積もりの基本情報
   * @throws EstimateBaseNotFoundException 見積もりの基本情報が見つからない場合にスローされる例外
   */
  private EstimateBase findEstimateBaseById(int estimateBaseId) {
    return estimateRepository.findEstimateBaseById(estimateBaseId)
        .orElseThrow(EstimateBaseNotFoundException::new);
  }

  /**
   * 指定されたメンテナンスIDと商品IDに基づいて、ガイド商品を取得する。 ガイドの許可を確認し、存在しない場合は例外をスローします。
   *
   * @param maintenanceId メンテナンスID
   * @param productId     商品ID
   * @return GuideProductPermission ガイド商品許可
   * @throws EstimateException.NoMatchProductException 商品が見つからない場合にスローされる例外
   */
  private GuideProductPermission fetchPermissionWithValidation(int maintenanceId, int productId) {
    return estimateRepository.findPermissionByMaintenanceIdAndProductId(maintenanceId, productId)
        .orElseThrow(EstimateException.NoMatchPermissionException::new);
  }

  /**
   * 商品IDを指定して、商品情報を取得する。
   *
   * @param productId 商品ID
   * @return Product 商品情報
   * @throws EstimateException.NoMatchProductException 商品が見つからない場合にスローされる例外
   */
  private Product findProduct(int productId) {
    return estimateRepository.findProductById(productId)
        .orElseThrow(EstimateException.NoMatchProductException::new);
  }

  /**
   * 車両に基づいてメンテナンスガイドを検索する。 車両のメーカー、モデル、年を使用して、対応するメンテナンスガイドを取得します。
   *
   * @param vehicle 車両情報
   * @return MaintenanceGuide メンテナンスガイド
   * @throws NoMatchMaintenanceGuideException メンテナンスガイドが見つからない場合にスローされる例外
   */
  private MaintenanceGuide searchMaintenanceGuideMatch(Vehicle vehicle) {
    return estimateRepository.findMaintenanceGuideByMakeAndModelAndYear(
            vehicle.getMake(),
            vehicle.getModel(),
            vehicle.getYear())
        .orElseThrow(NoMatchMaintenanceGuideException::new);
  }

  /**
   * 顧客IDを指定して、顧客情報を取得します。
   *
   * @param customerId 顧客ID
   * @return Customer 顧客情報
   * @throws CustomerException.CustomerNotFoundException 顧客が見つからない場合にスローされる例外
   */
  private Customer findCustomerById(int customerId) {
    return estimateRepository.findCustomerById(customerId)
        .orElseThrow(CustomerException.CustomerNotFoundException::new);
  }

  /**
   * 顧客IDを指定して、顧客の住所情報を取得します。
   *
   * @param customerId 顧客ID
   * @return CustomerAddress 顧客の住所情報
   * @throws CustomerAddressException.CustomerAddressNotFoundException 顧客の住所が見つからない場合にスローされる例外
   */
  private CustomerAddress findCustomerAddressByCustomerId(int customerId) {
    return estimateRepository.findCustomerAddressByCustomerId(customerId)
        .orElseThrow(CustomerAddressException.CustomerAddressNotFoundException::new);
  }

  /**
   * メンテナンスIDを指定して、車両名を取得します。 見つからない場合は、NoMatchMaintenanceGuideExceptionをスローします。
   *
   * @param maintenanceId メンテナンスID
   * @return 車両名
   * @throws EstimateException.NoMatchMaintenanceGuideException メンテナンスガイドが見つからない場合にスローされる例外
   */
  private String getVehicleNameByMaintenanceId(int maintenanceId) {
    return estimateRepository.findVehicleNameByMaintenanceId(maintenanceId)
        .orElseThrow(EstimateException.NoMatchMaintenanceGuideException::new);
  }

  /**
   * 見積もりベースを削除します。紐づいている見積もり商品も削除されます。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @throws EstimateBaseNotFoundException 見積もりの基本情報が見つからない場合にスローされる例外
   */
  @Transactional
  public void deleteEstimateBase(int estimateBaseId) {
    estimateRepository.deleteEstimateBaseById(estimateBaseId);
  }

  /**
   * 見積もり商品を削除します。
   *
   * @param estimateProductId 見積もり商品のID
   * @throws EstimateException.EstimateProductNotFoundException 見積もり商品が見つからない場合にスローされる例外
   */
  @Transactional
  public void deleteEstimateProduct(int estimateProductId) {
    estimateRepository.deleteEstimateProductById(estimateProductId);
  }

  /**
   * 見積もり商品の全てを見積もりベースIDで削除します。 見積もりベースは削除されませんが、紐づいている全ての見積もり商品が削除されます。
   *
   * @param estimateBaseId 見積もりの基本情報を取得するためのID
   * @throws EstimateException.EstimateProductNotFoundException 見積もり商品が見つからない場合にスローされる例外
   */
  @Transactional
  public void deleteEstimateProductsAllByEstimateBaseId(int estimateBaseId) {
    estimateRepository.deleteEstimateProductsByEstimateBaseId(estimateBaseId);
  }

  /**
   * 見積もり商品IDを指定して、見積もり商品を取得します。
   *
   * @param estimateProductId 見積もり商品のID
   * @return EstimateProduct 見積もり商品
   * @throws EstimateException.EstimateProductNotFoundException 見積もり商品が見つからない場合にスローされる例外
   */
  @Transactional
  private EstimateProduct findEstimateProductById(int estimateProductId) {
    return estimateRepository.findEstimateProductById(estimateProductId)
        .orElseThrow(EstimateException.EstimateProductNotFoundException::new);
  }

  /**
   * 見積もり商品を更新します。 見積もり商品IDを指定して、見積もり商品の情報を更新します。 更新後、合計金額を再計算して保存します。
   *
   * @param estimateProductId            見積もり商品のID
   * @param estimateProductUpdateRequest 更新する見積もり商品のリクエスト
   * @throws EstimateException.EstimateProductNotFoundException 見積もり商品が見つからない場合にスローされる例外
   */
  @Transactional
  public void updateEstimateProduct(int estimateProductId,
      EstimateProductUpdateRequest estimateProductUpdateRequest) {
    findEstimateProductById(estimateProductId);
    EstimateProduct estimateProduct = EstimateProductUpdateConverter.toDto(
        estimateProductUpdateRequest);
    estimateProduct.setEstimateProductId(estimateProductId);
    estimateProduct.setTotalPrice(
        calculateTotalPrice(estimateProduct.getUnitPrice(), estimateProduct.getQuantity()));
    estimateRepository.updateEstimateProduct(estimateProduct);
  }
}
