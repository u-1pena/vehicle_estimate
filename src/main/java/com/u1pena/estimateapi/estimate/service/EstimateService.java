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
import com.u1pena.estimateapi.estimate.converter.EstimateInformationConverter;
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
    return EstimateInformationConverter.toDto(
        estimateBaseId, estimateBase.getEstimateDate(), customerResponse, customerAddressResponse,
        vehicleDto);
  }

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
    List<EstimateProductJoinResult> result = estimateRepository.findProductDetailByProductId(
        productDetailList);

    return result.stream()
        .map(joinResult -> {
          EstimateProduct estimateProduct = estimateProductMap.get(joinResult.getProductId());
          return EstimateProductsConverter.toDto(joinResult, estimateProduct);
        })
        .toList();
  }

  private Customer findCustomerById(int customerId) {
    return estimateRepository.findCustomerById(customerId)
        .orElseThrow(CustomerException.CustomerNotFoundException::new);
  }

  private CustomerAddress findCustomerAddressByCustomerId(int customerId) {
    return estimateRepository.findCustomerAddressByCustomerId(customerId)
        .orElseThrow(CustomerAddressException.CustomerAddressNotFoundException::new);
  }

  private String getVehicleNameByMaintenanceId(int maintenanceId) {
    return estimateRepository.findVehicleNameByMaintenanceId(maintenanceId)
        .orElseThrow(EstimateException.NoMatchMaintenanceGuideException::new);
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

    EstimateBase estimateBase = findEstimateBaseById(estimateBaseId);
    Product product = findValidProduct(
        estimateBase.getMaintenanceId(), estimateProductCreateRequest.getProductId());

    if (product.getCategoryId() == CATEGORY_OIL) { // オイルの場合
      boolean existOil = estimateRepository.existOilProductsByEstimateBaseId(estimateBaseId);
      if (existOil) {
        int estimateProductId = estimateRepository.findEstimateProductIdByEstimateBaseId(
            estimateBaseId);
        double oilQuantityWithFilter = estimateRepository.findOilQuantityWithFilterByMaintenanceId(
            estimateBase.getMaintenanceId());
        estimateRepository.updateOilQuantityWithEstimateProductId(estimateProductId,
            oilQuantityWithFilter);
      }
    }
    if (product.getCategoryId() == CATEGORY_OIL_FILTER) {
      boolean existOilFilter = estimateRepository.existOilFilterProductsByEstimateBaseId(
          estimateBaseId);
      if (existOilFilter) {
        double oilQuantityWithFilter = estimateRepository.findOilQuantityWithFilterByMaintenanceId(
            estimateBase.getMaintenanceId());
        estimateProductCreateRequest.setQuantity(oilQuantityWithFilter);
      }
    }
    double quantity = resolveQuantity(estimateBase.getMaintenanceId(),
        product.getProductId(), estimateProductCreateRequest.getQuantity());

    BigDecimal unitPrice = product.getPrice();
    BigDecimal totalPrice = calculateTotalPrice(unitPrice,
        quantity);

    EstimateProductContext estimateProductContext = EstimateProductContext.builder()
        .estimateBaseId(estimateBaseId)
        .product(product)
        .quantity(quantity)
        .unitPrice(unitPrice)
        .totalPrice(totalPrice)
        .build();

    EstimateProduct estimateProduct = EstimateProductCreateConverter.toEntity(
        estimateProductContext);
    estimateRepository.insertEstimateProduct(estimateProduct);
  }

  private Vehicle findVehicleById(int vehicleId) {
    return estimateRepository.findVehicleByVehicleId(vehicleId)
        .orElseThrow(VehicleNotFoundException::new);
  }

  private EstimateBase findEstimateBaseById(int estimateBaseId) {
    return estimateRepository.findEstimateBaseById(estimateBaseId)
        .orElseThrow(EstimateBaseNotFoundException::new);
  }

  private Product findValidProduct(int maintenanceId, int productId) {
    GuideProductPermission guideProductPermission = findPermissionOrThrow(maintenanceId, productId);
    return estimateRepository.findProductById(guideProductPermission.getProductId())
        .orElseThrow(EstimateException.NoMatchProductException::new);
  }

  private MaintenanceGuide searchMaintenanceGuideMatch(Vehicle vehicle) {
    return estimateRepository.findMaintenanceGuideByMakeAndModelAndYear(
            vehicle.getMake(),
            vehicle.getModel(),
            vehicle.getYear())
        .orElseThrow(NoMatchMaintenanceGuideException::new);
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

  private Double resolveQuantity(int maintenanceId, int productId, Double quantity) {
    GuideProductPermission permission = findPermissionOrThrow(maintenanceId, productId);
    if (permission.isAutoAdjustQuantity()) {
      // permissionのquantityがnullの場合は、permissionのquantityを設定する
      return (quantity != null) ? quantity : permission.getQuantity();
    }
    return 1.0; // autoAdjustQuantityがfalseの場合は、1.0を返す
  }

  private GuideProductPermission findPermissionOrThrow(int maintenanceId,
      int productId) {
    return estimateRepository.findPermissionByMaintenanceIdAndProductId(maintenanceId, productId)
        .orElseThrow(EstimateException.NoMatchPermissionException::new);
  }

  public void deleteEstimateBase(int estimateBaseId) {
    EstimateBase estimateBase = findEstimateBaseById(estimateBaseId);
    estimateRepository.deleteEstimateBaseById(estimateBase.getEstimateBaseId());
  }

  public void deleteEstimateProduct(int estimateProductId) {
    EstimateProduct estimateProduct = findEstimateProductIdById(estimateProductId);
    estimateRepository.deleteEstimateProductById(estimateProduct.getEstimateProductId());
  }

  public void deleteEstimateProductsAllByEstimateBaseId(int estimateBaseId) {
    findEstimateProductsByEstimateBaseId(estimateBaseId);
    estimateRepository.deleteEstimateProductsByEstimateBaseId(estimateBaseId);
  }

  private EstimateProduct findEstimateProductIdById(int estimateProductId) {
    return estimateRepository.findEstimateProductById(estimateProductId)
        .orElseThrow(EstimateException.EstimateProductNotFoundException::new);
  }

  private void findEstimateProductsByEstimateBaseId(int estimateBaseId) {
    List<EstimateProduct> estimateProduct = estimateRepository
        .findEstimateProductsByEstimateBaseId(estimateBaseId);
    if (isEmpty(estimateProduct)) {
      throw new EstimateException.EstimateProductNotFoundException();
    }
  }

  public void updateEstimateProduct(int estimateProductId,
      EstimateProductUpdateRequest estimateProductUpdateRequest) {
    findEstimateProductIdById(estimateProductId);
    EstimateProduct estimateProduct = EstimateProductUpdateConverter.toDto(
        estimateProductUpdateRequest);
    estimateProduct.setEstimateProductId(estimateProductId);
    estimateProduct.setTotalPrice(
        calculateTotalPrice(estimateProduct.getUnitPrice(), estimateProduct.getQuantity()));
    estimateRepository.updateEstimateProduct(estimateProduct);
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
}
