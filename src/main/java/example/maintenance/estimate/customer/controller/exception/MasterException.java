package example.maintenance.estimate.customer.controller.exception;

public final class MasterException {

  private MasterException() {

  }

  public static class MaintenanceGuideAlreadyExistsException extends RuntimeException {

    public MaintenanceGuideAlreadyExistsException() {
      super("Maintenance guide already exists");
    }

    public MaintenanceGuideAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class ProductCategoryAlreadyExistsException extends RuntimeException {

    public ProductCategoryAlreadyExistsException() {
      super("Product category already exists");
    }

    public ProductCategoryAlreadyExistsException(String message) {
      super(message);
    }
  }
}
