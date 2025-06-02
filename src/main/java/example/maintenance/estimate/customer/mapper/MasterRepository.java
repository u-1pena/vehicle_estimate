package example.maintenance.estimate.customer.mapper;

import example.maintenance.estimate.customer.entity.enums.CarWashSize;
import example.maintenance.estimate.customer.entity.master.GuideProductPermission;
import example.maintenance.estimate.customer.entity.master.MaintenanceGuide;
import example.maintenance.estimate.customer.entity.master.Product;
import example.maintenance.estimate.customer.entity.master.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MasterRepository {

  void createMaintenanceGuide(MaintenanceGuide maintenanceGuide);

  void createProductCategory(ProductCategory productCategory);

  void createProduct(Product product);

  void createGuideProductPermission(int maintenanceId, int categoryId, int productId);

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
