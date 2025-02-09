package yuichi.user.management.controller.exception;

public class UserPaymentException {

  public static class UserPaymentAlreadyExistsException extends RuntimeException {

    public UserPaymentAlreadyExistsException() {
      super("User payment already exists");
    }

    public UserPaymentAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class PaymentExpirationInvalidException extends RuntimeException {

    public PaymentExpirationInvalidException() {
      super("Expiration date is invalid");
    }

    public PaymentExpirationInvalidException(String message) {
      super(message);
    }
  }

  public static class NotExistCardBrandException extends RuntimeException {

    public NotExistCardBrandException() {
      super("Card brand is invalid");
    }

    public NotExistCardBrandException(String message) {
      super(message);
    }
  }

}
