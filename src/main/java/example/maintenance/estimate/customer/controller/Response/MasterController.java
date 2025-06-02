package example.maintenance.estimate.customer.controller.Response;

import example.maintenance.estimate.customer.dto.request.master.MaintenanceGuideCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCategoryCreateRequest;
import example.maintenance.estimate.customer.dto.request.master.ProductCreateRequest;
import example.maintenance.estimate.customer.entity.master.MaintenanceGuide;
import example.maintenance.estimate.customer.entity.master.Product;
import example.maintenance.estimate.customer.entity.master.ProductCategory;
import example.maintenance.estimate.customer.service.MaintenanceGuideService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class MasterController {

  final MaintenanceGuideService maintenanceGuideService;

  public MasterController(MaintenanceGuideService maintenanceGuideService) {
    this.maintenanceGuideService = maintenanceGuideService;
  }

  @PostMapping("/maintenance-guides")
  public ResponseEntity<GlobalResponse> createMaintenanceGuide(
      @RequestBody @Valid MaintenanceGuideCreateRequest maintenanceGuideCreateRequest
      , UriComponentsBuilder uriComponentsBuilder) {
    MaintenanceGuide maintenanceGuide = maintenanceGuideService.registerMaintenanceGuide(
        maintenanceGuideCreateRequest);
    URI location = uriComponentsBuilder.path("/maintenance-guides/{id}")
        .buildAndExpand(maintenanceGuide.getMaintenanceId())
        .toUri();
    GlobalResponse body = new GlobalResponse("Maintenance guide created successfully");
    return ResponseEntity.created(location).body(body);
  }

  @PostMapping("/product-categories")
  public ResponseEntity<GlobalResponse> createProductCategory(
      @RequestBody @Valid ProductCategoryCreateRequest productCategoryCreateRequest
      , UriComponentsBuilder uriComponentsBuilder) {
    ProductCategory productCategory = maintenanceGuideService.registerProductCategory(
        productCategoryCreateRequest);
    URI location = uriComponentsBuilder.path("/product-categories/{id}")
        .buildAndExpand(productCategory.getCategoryId())
        .toUri();
    GlobalResponse body = new GlobalResponse("Product category created successfully");
    return ResponseEntity.created(location).body(body);
  }

  @PostMapping("/products")
  public ResponseEntity<GlobalResponse> createProduct(
      @RequestBody @Valid ProductCreateRequest productCreateRequest
      , UriComponentsBuilder uriComponentsBuilder) {
    Product product = maintenanceGuideService.registerProduct(
        productCreateRequest);
    URI location = uriComponentsBuilder.path("/products/{id}")
        .buildAndExpand(product.getProductId())
        .toUri();
    GlobalResponse body = new GlobalResponse("Product created successfully");
    return ResponseEntity.created(location).body(body);
  }
}
