package example.maintenance.estimate.customer.controller.exception;

public final class EstimateException {

  private EstimateException() {
    // Prevent instantiation
  }

  public static class MaintenanceGuideNotFoundException extends RuntimeException {

    public MaintenanceGuideNotFoundException() {
      super("MaintenanceGuide not found for vehicle spec");
    }

    public MaintenanceGuideNotFoundException(String message) {
      super(message);
    }
  }
}
