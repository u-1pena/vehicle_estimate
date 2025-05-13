package example.maintenance.estimate.customer.controller.exception;

public final class CustomerException {

  private CustomerException() {
    // Prevent instantiation
  }

  public static class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
      super("Customer not found");
    }

    public CustomerNotFoundException(String message) {
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

  public static class AlreadyExistsPhoneNumberException extends RuntimeException {

    public AlreadyExistsPhoneNumberException() {
      super("Phone number already exists");
    }

    public AlreadyExistsPhoneNumberException(String message) {
      super(message);
    }
  }

  public static class InvalidSearchParameterException extends RuntimeException {

    public InvalidSearchParameterException() {
      super("Invalid search parameter");
    }

    public InvalidSearchParameterException(String message) {
      super(message);
    }
  }
}
