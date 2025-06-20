package com.u1pena.estimateapi.master.repository;

import com.u1pena.estimateapi.common.enums.CarWashSize;
import com.u1pena.estimateapi.estimate.dto.GuideProductPermissionCreateContext;
import com.u1pena.estimateapi.master.entity.GuideProductPermission;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.entity.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MasterRepository {

  void createMaintenanceGuide(MaintenanceGuide maintenanceGuide);

  void createProductCategory(ProductCategory productCategory);

  void createProduct(Product product);

  void createGuideProductPermission(GuideProductPermissionCreateContext context);

  Optional<MaintenanceGuide> findMaintenanceGuideByMakeAndModelAndTypeAndYear(
      MaintenanceGuide maintenanceGuide);

  List<Product> findProductByOilViscosity(String oilViscosity);

  List<Product> findProductByCarWashSize(CarWashSize carWashSize);

  List<Product> findProductByOilFilterPartNumber(String oilFilterPartNumber);

  Optional<ProductCategory> findProductCategoryByName(String categoryName);

  List<MaintenanceGuide> findAllMaintenanceGuides();

  List<ProductCategory> findAllProductCategories();

  List<Product> findAllProducts();

  List<GuideProductPermission> findAllGuideProductPermissions();
}
