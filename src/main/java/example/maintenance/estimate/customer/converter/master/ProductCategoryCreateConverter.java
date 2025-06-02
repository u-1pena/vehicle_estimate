package example.maintenance.estimate.customer.converter.master;

import example.maintenance.estimate.customer.dto.request.master.ProductCategoryCreateRequest;
import example.maintenance.estimate.customer.entity.master.ProductCategory;

public class ProductCategoryCreateConverter {

  public static ProductCategory productCategoryConvertToEntity(
      ProductCategoryCreateRequest productCategoryCreateRequest) {
    return ProductCategory.builder()
        .categoryName(productCategoryCreateRequest.getCategoryName())
        .build();
  }
}
