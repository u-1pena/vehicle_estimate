package com.u1pena.estimateapi.estimate.controller;

import com.u1pena.estimateapi.common.response.GlobalResponse;
import com.u1pena.estimateapi.estimate.dto.request.EstimateProductCreateRequest;
import com.u1pena.estimateapi.estimate.dto.response.EstimateFullResponse;
import com.u1pena.estimateapi.estimate.service.EstimateService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstimateController {

  private final EstimateService estimateService;

  public EstimateController(EstimateService estimateService) {
    this.estimateService = estimateService;
  }

  @PostMapping("/estimates/vehicle/{vehicleId}")
  public ResponseEntity<GlobalResponse> createEstimate(
      @PathVariable int vehicleId) {
    estimateService.registerEstimateBase(vehicleId);
    GlobalResponse response = new GlobalResponse("Estimate created successfully");
    return ResponseEntity.created(URI.create("/estimates/vehicle/" + vehicleId)).body(response);
  }

  @PostMapping("/estimates/{estimateBaseId}/products")
  public ResponseEntity<GlobalResponse> createEstimateProduct(
      @PathVariable int estimateBaseId,
      @RequestBody @Valid EstimateProductCreateRequest estimateProductCreateRequest) {
    estimateService.registerEstimateProduct(estimateBaseId, estimateProductCreateRequest);
    GlobalResponse response = new GlobalResponse("Estimate product created successfully");
    return ResponseEntity.created(URI.create("/estimates/" + estimateBaseId + "/estimate-products"))
        .body(response);
  }

  @GetMapping("/estimates/full/{estimateId}")
  public EstimateFullResponse searchEstimateFullByEstimateId(
      @PathVariable int estimateId) {
    return estimateService.getEstimateFullById(estimateId);
  }
}
