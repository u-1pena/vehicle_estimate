package yuichi.user.management.controller.exception;

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
public class UserExceptionHandler extends RuntimeException {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFoundException(
      UserNotFoundException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "404",
        "error", "Not Found",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
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

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
      UserAlreadyExistsException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "422",
        "error", "Unprocessable Entity",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(UserDetailAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(
      UserDetailAlreadyExistsException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "422",
        "error", "Unprocessable Entity",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(UserPaymentAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserPaymentAlreadyExistsException(
      UserPaymentAlreadyExistsException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "422",
        "error", "Unprocessable Entity",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "400",
        "error", "Bad Request",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PaymentExpirationInvalidException.class)
  public ResponseEntity<Map<String, String>> handlePaymentExpirationInvalidException(
      PaymentExpirationInvalidException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "status", "400",
        "error", "Bad Request",
        "message", e.getMessage(),
        "path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
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
