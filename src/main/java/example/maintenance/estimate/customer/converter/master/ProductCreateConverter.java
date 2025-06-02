package example.maintenance.estimate.customer.converter.master;

import example.maintenance.estimate.customer.dto.request.master.ProductCreateRequest;
import example.maintenance.estimate.customer.entity.master.Product;

public class ProductCreateConverter {

  public static Product productCreateConvertToEntity(
      ProductCreateRequest productCreateRequest) {
    return Product.builder()
        .categoryId(productCreateRequest.getCategoryId())
        .productName(productCreateRequest.getProductName())
        .description(productCreateRequest.getDescription())
        .guideMatchKey(productCreateRequest.getGuideMatchKey())
        .price(productCreateRequest.getPrice())
        .build();
  }
}
