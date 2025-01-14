package yuichi.user.management.controller.exception;

public class UserDetailAlreadyExistsException extends RuntimeException {

  public UserDetailAlreadyExistsException(String message) {
    super(message);
  }

}
