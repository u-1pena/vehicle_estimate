package example.maintenance.estimate.customer.dto.request.master;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCategoryCreateRequest {

  @NotBlank(message = "カテゴリー名を入力してください")
  private String categoryName;

}
