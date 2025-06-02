package example.maintenance.estimate.customer.entity.master;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class GuideProductPermission {

  private int maintenanceId;
  private int categoryId;
  private int productId;

  public GuideProductPermission(int maintenanceId, int categoryId, int productId) {
    this.maintenanceId = maintenanceId;
    this.categoryId = categoryId;
    this.productId = productId;
  }
}
