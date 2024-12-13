package yuichi.user.management.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
