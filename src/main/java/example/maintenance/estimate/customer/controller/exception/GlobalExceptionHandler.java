package example.maintenance.estimate.customer.controller.exception;

import example.maintenance.estimate.customer.controller.exception.CustomerAddressException.CustomerAddressAlreadyException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.AlreadyExistsEmailException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.AlreadyExistsPhoneNumberException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.CustomerNotFoundException;
import example.maintenance.estimate.customer.controller.exception.CustomerException.InvalidSearchParameterException;
import example.maintenance.estimate.customer.controller.exception.MasterException.MaintenanceGuideAlreadyExistsException;
import example.maintenance.estimate.customer.controller.exception.MasterException.ProductCategoryAlreadyExistsException;
import example.maintenance.estimate.customer.controller.exception.VehicleException.AlreadyExistsVehicleException;
import example.maintenance.estimate.customer.controller.exception.VehicleException.VehicleInactiveException;
import example.maintenance.estimate.customer.controller.exception.VehicleException.VehicleYearInvalidException;
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

  @ExceptionHandler(CustomerAddressException.CustomerAddressAlreadyException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
      CustomerAddressAlreadyException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(CustomerAddressException.CustomerAddressNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserAddressNotFoundException(
      CustomerAddressException.CustomerAddressNotFoundException e, HttpServletRequest request) {
    return new ResponseEntity<>(
        createErrorResponse(HttpStatus.NOT_FOUND, e, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(VehicleException.VehicleNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleVehicleNotFoundException(
      VehicleException.VehicleNotFoundException e, HttpServletRequest request) {
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
