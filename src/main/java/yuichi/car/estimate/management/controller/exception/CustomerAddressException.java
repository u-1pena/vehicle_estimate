package yuichi.car.estimate.management.controller.exception;

public class CustomerAddressException {


  public static class CustomerAddressAlreadyException extends RuntimeException {

    public CustomerAddressAlreadyException() {
      super("Customer address already exists");
    }

    public CustomerAddressAlreadyException(String message) {
      super(message);
    }
  }
}
