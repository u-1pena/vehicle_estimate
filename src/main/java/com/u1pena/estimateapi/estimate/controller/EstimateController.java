package com.u1pena.estimateapi.estimate.controller;

import com.u1pena.estimateapi.common.response.GlobalResponse;
import com.u1pena.estimateapi.estimate.dto.request.EstimateBaseCreateRequest;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductCreateRequest;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductUpdateRequest;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryDateResponse;
import com.u1pena.estimateapi.estimate.dto.response.EstimateSummaryResponse;
import com.u1pena.estimateapi.estimate.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Estimate", description = "見積もりに関するAPI")
@RestController
public class EstimateController {

  private final EstimateService estimateService;

  public EstimateController(EstimateService estimateService) {
    this.estimateService = estimateService;
  }

  /**
   * 見積もりのベースを作成します。
   *
   * @param estimateBaseCreateRequest
   * @param uriBuilder
   * @return 見積もりの作成結果を含むレスポンスエンティティ
   */
  @Operation(summary = "見積もりベースを作成する処理"
      , description = "指定された車両IDに基づいて新しい見積もりを作成します。")
  @Parameter(name = "estimateBaseCreateRequest", description = "見積もりベースの作成リクエスト")
  @PostMapping("/estimates")
  public ResponseEntity<GlobalResponse> createEstimate(
      @RequestBody @Valid EstimateBaseCreateRequest estimateBaseCreateRequest,
      UriComponentsBuilder uriBuilder) {
    int vehicleId = estimateBaseCreateRequest.getVehicleId();
    int estimateBaseId = estimateService.registerEstimateBase(vehicleId);
    URI location = uriBuilder
        .path("/estimates/{estimateId}")
        .buildAndExpand(estimateBaseId)
        .toUri();
    GlobalResponse response = new GlobalResponse("Estimate created successfully");
    return ResponseEntity.created(location).body(response);
  }

  /**
   * 商品の見積もりを作成します。商品ガイドに基づいて、必要な商品を登録します。
   *
   * @param estimateBaseId
   * @param estimateProductCreateRequest
   * @param uriBuilder
   * @return 見積もり商品の作成結果を含むレスポンスエンティティ
   */
  @Operation(summary = "見積もり商品を作成する処理"
      , description = "指定された見積もりIDに基づいて新しい見積もり商品を作成します。")
  @Parameter(name = "estimateBaseId", description = "見積もりID")
  @PostMapping("/estimates/{estimateBaseId}/products")
  public ResponseEntity<GlobalResponse> createEstimateProduct(
      @PathVariable int estimateBaseId,
      @RequestBody @Valid EstimateProductCreateRequest estimateProductCreateRequest,
      UriComponentsBuilder uriBuilder) {
    estimateService.registerEstimateProduct(estimateBaseId, estimateProductCreateRequest);
    URI location = uriBuilder
        .path("/estimates/{estimateBaseId}/products")
        .buildAndExpand(estimateBaseId)
        .toUri();
    GlobalResponse response = new GlobalResponse("Estimate product created successfully");
    return ResponseEntity.created(location).body(response);
  }

  /**
   * 見積もり番号から見積もり情報を取得します。 ヘッダー：顧客情報、車両情報 プロダクト：見積もり商品情報 合計金額：見積もりの合計金額
   *
   * @param estimateBaseId
   * @return 指定した見積もりIDの見積詳細
   */
  @Operation(summary = "見積もり情報を取得する処理"
      , description = "指定された見積もりIDに基づいて見積もり情報を取得します。")
  @Parameter(name = "estimateBaseId", description = "見積もりID")
  @GetMapping("/estimates/full/{estimateBaseId}")
  public EstimateFullResponse searchEstimateFullByEstimateId(
      @PathVariable int estimateBaseId) {
    return estimateService.getEstimateFullById(estimateBaseId);
  }

  /**
   * 顧客IDから全ての見積概要を一覧取得します。
   *
   * @param customerId
   * @return 指定した顧客IDの見積もり商品情報リスト
   */
  @Operation(summary = "顧客IDから見積もり概要を取得する処理"
      , description = "指定された顧客IDに基づいて見積もり概要を取得します。")
  @Parameter(name = "customerId", description = "顧客ID")
  @GetMapping("/estimates/customers/{customerId}")
  public List<EstimateSummaryResponse> searchEstimateSummaryByCustomerId(
      @PathVariable int customerId) {
    return estimateService.getEstimateSummaryByCustomerId(customerId);
  }

  /**
   * 見積もり情報を日付範囲で取得します。
   *
   * @param startDate 開始日付
   * @param endDate   終了日付
   * @return 指定した日付範囲の見積もり概要リスト
   */
  @Operation(summary = "日付範囲で見積もり概要を取得する処理"
      , description = "指定された開始日付と終了日付に基づいて見積もり概要を取得します。")
  @Parameter(name = "startDate", description = "開始日付", example = "2023-01-01")
  @GetMapping("/estimates")
  public ResponseEntity<List<EstimateSummaryDateResponse>> getEstimatesBetweenDates(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
  ) {
    List<EstimateSummaryDateResponse> result = estimateService.getEstimatesByDateRange(startDate,
        endDate);
    return ResponseEntity.ok(result);
  }

  /**
   * 指定した見積もりIDの見積もりを削除します。見積もりベースを削除すると見積もり商品も削除されます。
   *
   * @param estimateId
   * @return 指定した見積もりIDの見積もり商品情報リスト
   */
  @Operation(summary = "見積もりを削除する処理"
      , description = "指定された見積もりIDに基づいて見積もりを削除します。")
  @Parameter(name = "estimateId", description = "見積もりID")
  @DeleteMapping("/estimates/{estimateId}")
  public ResponseEntity<Void> deleteEstimateById(
      @PathVariable int estimateId) {
    estimateService.deleteEstimateBase(estimateId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定した見積もりIDの見積もり商品を削除します。
   *
   * @param estimateProductId
   * @return 指定した見積もりIDの見積もり商品情報リスト
   */
  @Operation(summary = "見積もり商品を削除する処理"
      , description = "指定された見積もり商品IDに基づいて見積もり商品を削除します。")
  @Parameter(name = "estimateProductId", description = "見積もり商品ID")
  @DeleteMapping("/estimates/products/{estimateProductId}")
  public ResponseEntity<Void> deleteEstimateProductById(
      @PathVariable int estimateProductId) {
    estimateService.deleteEstimateProduct(estimateProductId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定した見積もりIDの全ての見積もり商品を削除します。見積もりベースは削除されません。
   *
   * @param estimateBaseId
   * @return 指定した見積もりIDの見積もり商品情報リスト
   */
  @Operation(summary = "見積もり商品の全削除処理"
      , description = "指定された見積もりIDに基づいて全ての見積もり商品を削除します。見積もりベースは削除されません。")
  @Parameter(name = "estimateBaseId", description = "見積もりID")
  @DeleteMapping("/estimates/{estimateBaseId}/products")
  public ResponseEntity<GlobalResponse> deleteEstimateProductsAllByEstimateBaseId(
      @PathVariable int estimateBaseId) {
    estimateService.deleteEstimateProductsAllByEstimateBaseId(estimateBaseId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定した見積もりIDの見積もり商品を更新します。
   *
   * @param estimateProductId
   * @param estimateProductUpdateRequest
   * @return 更新された見積もり商品情報
   */
  @Operation(summary = "見積もり商品を更新する処理"
      , description = "指定された見積もり商品IDに基づいて見積もり商品を更新します。")
  @Parameter(name = "estimateProductId", description = "見積もり商品ID")
  @PutMapping("/estimates/products/{estimateProductId}")
  public ResponseEntity<Void> updateEstimateProduct(
      @PathVariable int estimateProductId,
      @RequestBody EstimateProductUpdateRequest estimateProductUpdateRequest) {
    estimateService.updateEstimateProduct(estimateProductId, estimateProductUpdateRequest);
    return ResponseEntity.noContent().build();
  }
}
