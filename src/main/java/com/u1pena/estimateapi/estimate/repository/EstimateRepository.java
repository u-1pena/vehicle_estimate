package com.u1pena.estimateapi.estimate.repository;

import com.u1pena.estimateapi.customer.entity.Customer;
import com.u1pena.estimateapi.customer.entity.CustomerAddress;
import com.u1pena.estimateapi.customer.entity.Vehicle;
import com.u1pena.estimateapi.estimate.dto.EstimateProductJoinResult;
import com.u1pena.estimateapi.estimate.dto.EstimateSummaryResult;
import com.u1pena.estimateapi.estimate.entity.EstimateBase;
import com.u1pena.estimateapi.estimate.entity.EstimateProduct;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EstimateRepository {

  void insertEstimateBase(EstimateBase estimateBase);

  Optional<Vehicle> findVehicleByVehicleId(int vehicleId);

  Optional<MaintenanceGuide> findMaintenanceGuideByMakeAndModelAndYear(
      String make, String model, YearMonth year);

  void insertEstimateProduct(EstimateProduct estimateProduct);

  Optional<EstimateBase> findEstimateBaseById(int estimateBaseId);

  Optional<GuideProductPermission> findPermissionByMaintenanceIdAndProductId(
      int maintenanceId, int productId);

  Optional<String> findVehicleNameByMaintenanceId(int maintenanceId);

  Optional<Product> findProductById(int productId);

  void updateOilQuantityWithEstimateProductId(
      @Param("estimateProductId") int estimateProductId,
      @Param("quantity") double quantity);

  int countOilProductsByEstimateBaseId(int estimateBaseId);

  default boolean existOilProductsByEstimateBaseId(int estimateBaseId) {
    return countOilProductsByEstimateBaseId(estimateBaseId) > 0;
  }

  int countOilFilterProductsByEstimateBaseId(int estimateBaseId);

  default boolean existOilFilterProductsByEstimateBaseId(int estimateBaseId) {
    return countOilFilterProductsByEstimateBaseId(estimateBaseId) > 0;
  }

  double findOilQuantityWithFilterByMaintenanceId(int maintenanceId);

  List<Integer> findEstimateProductIdByEstimateBaseId(int estimateBaseId);

  int findProductsWithOilCategoryByEstimateBaseId(int estimateBaseId);

  int findEstimateProductIdByEstimateBaseIdAndProductId(
      @Param("estimateBaseId") int estimateBaseId,
      @Param("productId") int productId);

  Optional<Customer> findCustomerById(int customerId);

  Optional<CustomerAddress> findCustomerAddressByCustomerId(int customerId);

  List<EstimateProduct> findEstimateProductsByEstimateBaseId(int estimateBaseId);

  List<EstimateProductJoinResult> findProductsWithCategoryByIds(List<Integer> productId);

  void deleteEstimateBaseById(int estimateBaseId);

  void deleteEstimateProductsByEstimateBaseId(int estimateBaseId);

  void deleteEstimateProductById(int estimateProductId);

  Optional<EstimateProduct> findEstimateProductById(int estimateProductId);

  void updateEstimateProduct(EstimateProduct estimateProduct);

  List<EstimateSummaryResult> findEstimateSummaryResultsByCustomerId(int customerId);

  List<EstimateSummaryResult> findEstimateSummaryResultsByDateRange(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate);

  List<EstimateBase> findAllEstimateBases();

  List<EstimateProduct> findAllEstimateProducts();
}
