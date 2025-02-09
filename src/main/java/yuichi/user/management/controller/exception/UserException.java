package yuichi.user.management.controller.exception;

public class UserException {

  public static class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
      super("User not found");
    }

    public UserNotFoundException(String message) {
      super(message);
    }
  }

  public static class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
      super("User already exists");
    }

    public UserAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class AlreadyExistsEmailException extends RuntimeException {

    public AlreadyExistsEmailException() {
      super("Email already exists");
    }

    public AlreadyExistsEmailException(String message) {
      super(message);
    }
  }
}
