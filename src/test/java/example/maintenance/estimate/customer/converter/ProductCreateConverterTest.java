package example.maintenance.estimate.customer.converter;

import static org.assertj.core.api.Assertions.assertThat;

import example.maintenance.estimate.customer.converter.master.ProductCreateConverter;
import example.maintenance.estimate.customer.dto.request.master.ProductCreateRequest;
import example.maintenance.estimate.customer.entity.master.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductCreateConverterTest {

  @Test
  void productCreateRequestをproductに変換できる() {
    ProductCreateRequest productCreateRequest = new ProductCreateRequest();
    productCreateRequest.setCategoryId(1);
    productCreateRequest.setProductName("Test Product");
    productCreateRequest.setDescription("This is a test product.");
    productCreateRequest.setGuideMatchKey("test-key");
    productCreateRequest.setPrice(BigDecimal.valueOf(1000));

    Product actual = ProductCreateConverter.productCreateConvertToEntity(productCreateRequest);
    // 検証
    assertThat(actual.getCategoryId()).isEqualTo(productCreateRequest.getCategoryId());
    assertThat(actual.getProductName()).isEqualTo(productCreateRequest.getProductName());
    assertThat(actual.getDescription()).isEqualTo(productCreateRequest.getDescription());
    assertThat(actual.getGuideMatchKey()).isEqualTo(productCreateRequest.getGuideMatchKey());
    assertThat(actual.getPrice()).isEqualTo(productCreateRequest.getPrice());
  }
}
