package yuichi.user.management.controller.exception;

public class PaymentExpirationInvalidException extends RuntimeException {

  public PaymentExpirationInvalidException(String message) {
    super(message);
  }
}
