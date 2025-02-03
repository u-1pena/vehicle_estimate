package yuichi.user.management.controller.exception;

public class UserDetailException {

  public static class BirthdayInvalidException extends RuntimeException {

    public BirthdayInvalidException() {
      super("Birthday is invalid");
    }

    public BirthdayInvalidException(String message) {
      super(message);
    }
  }

  public static class UserDetailAlreadyExistsException extends RuntimeException {

    public UserDetailAlreadyExistsException() {
      super("User detail already exists");
    }

    public UserDetailAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class AlreadyExistsMobileNumberException extends RuntimeException {

    public AlreadyExistsMobileNumberException() {
      super("Mobile number already exists");
    }

    public AlreadyExistsMobileNumberException(String message) {
      super(message);
    }
  }
}
