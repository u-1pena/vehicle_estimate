package example.maintenance.estimate.customer.converter;

import static org.assertj.core.api.Assertions.assertThat;

import example.maintenance.estimate.customer.converter.master.ProductCategoryCreateConverter;
import example.maintenance.estimate.customer.dto.request.master.ProductCategoryCreateRequest;
import example.maintenance.estimate.customer.entity.master.ProductCategory;
import org.junit.jupiter.api.Test;

class ProductCategoryCreateConverterTest {

  @Test
  void productCategoryCreateRequestをproductCategoryに変換できること() {
    // Arrange
    ProductCategoryCreateRequest productCategoryCreateRequest = new ProductCategoryCreateRequest();
    productCategoryCreateRequest.setCategoryName("Test Category");

    // Act
    ProductCategory actual = ProductCategoryCreateConverter.toEntity(
        productCategoryCreateRequest);
    // Assert
    assertThat(actual.getCategoryName()).isEqualTo(productCategoryCreateRequest.getCategoryName());
  }
}
