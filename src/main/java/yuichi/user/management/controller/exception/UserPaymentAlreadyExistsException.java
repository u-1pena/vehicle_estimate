package yuichi.user.management.controller.exception;

public class UserPaymentAlreadyExistsException extends RuntimeException {

  public UserPaymentAlreadyExistsException(String message) {
    super(message);
  }

}
