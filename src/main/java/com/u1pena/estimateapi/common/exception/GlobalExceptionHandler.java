package com.u1pena.estimateapi.common.exception;

import com.u1pena.estimateapi.customer.exception.CustomerAddressException.CustomerAddressAlreadyException;
import com.u1pena.estimateapi.customer.exception.CustomerAddressException.CustomerAddressNotFoundException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsEmailException;
import com.u1pena.estimateapi.customer.exception.CustomerException.AlreadyExistsPhoneNumberException;
import com.u1pena.estimateapi.customer.exception.CustomerException.CustomerNotFoundException;
import com.u1pena.estimateapi.customer.exception.CustomerException.InvalidSearchParameterException;
import com.u1pena.estimateapi.customer.exception.VehicleException.AlreadyExistsVehicleException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleInactiveException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleNotFoundException;
import com.u1pena.estimateapi.customer.exception.VehicleException.VehicleYearInvalidException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.EstimateBaseNotFoundException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.EstimateProductNotFoundException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.ExistOilFilterProductsException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.ExistOilProductsException;
import com.u1pena.estimateapi.estimate.exception.EstimateException.NoMatchMaintenanceGuideException;
import com.u1pena.estimateapi.master.exception.MasterException.MaintenanceGuideAlreadyExistsException;
import com.u1pena.estimateapi.master.exception.MasterException.ProductCategoryAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCustomerNotFoundException(
      CustomerNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    List<Map<String, String>> errors = new ArrayList<>();
    e.getBindingResult().getFieldErrors().forEach(fieldError -> {
      Map<String, String> error = new HashMap<>();
      error.put("field", fieldError.getField());
      error.put("message", fieldError.getDefaultMessage());
      errors.add(error);
    });
    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error", errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(AlreadyExistsEmailException.class)
  public ResponseEntity<Map<String, String>> handleAlreadyExistsEmailException(
      AlreadyExistsEmailException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(AlreadyExistsPhoneNumberException.class)
  public ResponseEntity<Map<String, String>> handleAlreadyExistsPhoneNumberException(
      AlreadyExistsPhoneNumberException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(CustomerAddressAlreadyException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
      CustomerAddressAlreadyException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(CustomerAddressNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserAddressNotFoundException(
      CustomerAddressNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(VehicleNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleVehicleNotFoundException(
      VehicleNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AlreadyExistsVehicleException.class)
  public ResponseEntity<Map<String, String>> handleAlreadyExistsVehicleException(
      AlreadyExistsVehicleException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(VehicleYearInvalidException.class)
  public ResponseEntity<Map<String, String>> handleVehicleYearInvalidException(
      VehicleYearInvalidException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.BAD_REQUEST, e, request), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidSearchParameterException.class)
  public ResponseEntity<Map<String, String>> handleInvalidSearchParameterException(
      InvalidSearchParameterException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.BAD_REQUEST, e, request), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(VehicleInactiveException.class)
  public ResponseEntity<Map<String, String>> handleVehicleInactiveException(
      VehicleInactiveException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.BAD_REQUEST, e, request), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaintenanceGuideAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleMaintenanceGuideAlreadyExistsException(
      MaintenanceGuideAlreadyExistsException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ProductCategoryAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleProductCategoryAlreadyExistsException(
      ProductCategoryAlreadyExistsException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(NoMatchMaintenanceGuideException.class)
  public ResponseEntity<Map<String, String>> handleEstimateNotFoundException(
      NoMatchMaintenanceGuideException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EstimateBaseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEstimateBaseNotFoundException(
      EstimateBaseNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EstimateProductNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEstimateProductNotFoundException(
      EstimateProductNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ExistOilFilterProductsException.class)
  public ResponseEntity<Map<String, String>> handleExistOilFilterProductsException(
      ExistOilFilterProductsException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ExistOilProductsException.class)
  public ResponseEntity<Map<String, String>> handleExistOilProductsException(
      ExistOilProductsException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private Map<String, String> createErrorResponse(
      HttpStatus status, Exception e, HttpServletRequest request) {
    return Map.of(
        "status", String.valueOf(status.value()),
        "error", status.getReasonPhrase(),
        "message", e.getMessage(),
        "path", request.getRequestURI());
  }


  public static final class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final List<Map<String, String>> errors;

    public ErrorResponse(HttpStatus status, String message, List<Map<String, String>> errors) {
      this.status = status;
      this.message = message;
      this.errors = errors;
    }

    public HttpStatus getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }

    public List<Map<String, String>> getErrors() {
      return errors;
    }
  }
}
