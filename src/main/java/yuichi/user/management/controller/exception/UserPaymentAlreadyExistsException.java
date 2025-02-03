package yuichi.user.management.controller.exception;

public class UserPaymentAlreadyExistsException extends RuntimeException {

  public UserPaymentAlreadyExistsException() {
    super("UserPayment already exists with cardNumber");
  }

  public UserPaymentAlreadyExistsException(String message) {
    super(message);
  }

}
