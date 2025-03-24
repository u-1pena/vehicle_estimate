package yuichi.car.estimate.management.controller.exception;

public class VehicleException extends RuntimeException {

  public static class AlreadyExistsVehicleException extends RuntimeException {

    public AlreadyExistsVehicleException() {
      super("Vehicle already exists");
    }

    public AlreadyExistsVehicleException(String message) {
      super(message);
    }
  }

  public static class VehicleYearInvalidException extends RuntimeException {

    public VehicleYearInvalidException() {
      super("Vehicle year is invalid.");
    }

    public VehicleYearInvalidException(String message) {
      super(message);
    }
  }
}
