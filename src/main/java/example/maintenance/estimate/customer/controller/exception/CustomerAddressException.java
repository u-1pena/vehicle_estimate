package example.maintenance.estimate.customer.controller.exception;

public final class CustomerAddressException {

  private CustomerAddressException() {
    // Prevent instantiation
  }

  public static class CustomerAddressAlreadyException extends RuntimeException {

    public CustomerAddressAlreadyException() {
      super("Customer address already exists");
    }

    public CustomerAddressAlreadyException(String message) {
      super(message);
    }
  }

  public static class CustomerAddressNotFoundException extends RuntimeException {

    public CustomerAddressNotFoundException() {
      super("Customer address not found");
    }

    public CustomerAddressNotFoundException(String message) {
      super(message);
    }
  }
}
