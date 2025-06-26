package com.u1pena.estimateapi.master.controller;

import com.u1pena.estimateapi.common.response.GlobalResponse;
import com.u1pena.estimateapi.master.dto.request.MaintenanceGuideCreateRequest;
import com.u1pena.estimateapi.master.dto.request.ProductCategoryCreateRequest;
import com.u1pena.estimateapi.master.dto.request.ProductCreateRequest;
import com.u1pena.estimateapi.master.entity.MaintenanceGuide;
import com.u1pena.estimateapi.master.entity.Product;
import com.u1pena.estimateapi.master.entity.ProductCategory;
import com.u1pena.estimateapi.master.service.MaintenanceGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Master", description = "マスター情報に関するAPI")
@RestController
public class MasterController {

  final MaintenanceGuideService maintenanceGuideService;

  public MasterController(MaintenanceGuideService maintenanceGuideService) {
    this.maintenanceGuideService = maintenanceGuideService;
  }

  /**
   * メンテナンスガイドを作成します。
   *
   * @param maintenanceGuideCreateRequest メンテナンスガイドの作成リクエスト
   * @param uriComponentsBuilder          URIビルダー
   * @return 作成されたメンテナンスガイドのURIを含むレスポンスエンティティ
   */
  @Operation(summary = "メンテナンスガイドを作成する処理"
      , description = "指定されたメンテナンスガイドの情報に基づいて新しいメンテナンスガイドを作成します。")
  @Parameter(name = "maintenanceGuideCreateRequest", description = "メンテナンスガイドの作成リクエスト")
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

  /**
   * 製品カテゴリを作成します。
   *
   * @param productCategoryCreateRequest 製品カテゴリの作成リクエスト
   * @param uriComponentsBuilder         URIビルダー
   * @return 作成された製品カテゴリのURIを含むレスポンスエンティティ
   */
  @Operation(summary = "製品カテゴリを作成する処理"
      , description = "指定された製品カテゴリの情報に基づいて新しい製品カテゴリを作成します。")
  @Parameter(name = "productCategoryCreateRequest", description = "製品カテゴリの作成リクエスト")
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

  /**
   * 製品を作成します。
   *
   * @param productCreateRequest 製品の作成リクエスト
   * @param uriComponentsBuilder URIビルダー
   * @return 作成された製品のURIを含むレスポンスエンティティ
   */
  @Operation(summary = "製品を作成する処理"
      , description = "指定された製品の情報に基づいて新しい製品を作成します。")
  @Parameter(name = "productCreateRequest", description = "製品の作成リクエスト")
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
