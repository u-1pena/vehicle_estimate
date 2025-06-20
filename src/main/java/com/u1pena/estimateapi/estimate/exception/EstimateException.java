package com.u1pena.estimateapi.estimate.exception;

public class EstimateException {

  private EstimateException() {

  }

  public static class NoMatchMaintenanceGuideException extends RuntimeException {

    public NoMatchMaintenanceGuideException() {
      super("No matching maintenance guide found");
    }

    public NoMatchMaintenanceGuideException(String message) {
      super(message);
    }
  }

  public static class NoMatchPermissionException extends RuntimeException {

    public NoMatchPermissionException() {
      super("No matching permission found for the product in the maintenance guide");
    }

    public NoMatchPermissionException(String message) {
      super(message);
    }
  }

  public static class NoMatchProductException extends RuntimeException {

    public NoMatchProductException() {
      super("No matching product found");
    }

    public NoMatchProductException(String message) {
      super(message);
    }
  }

  public static class EstimateBaseNotFoundException extends RuntimeException {

    public EstimateBaseNotFoundException() {
      super("Estimate base not found");
    }

    public EstimateBaseNotFoundException(String message) {
      super(message);
    }
  }

  public static class EstimateProductNotFoundException extends RuntimeException {

    public EstimateProductNotFoundException() {
      super("Estimate product not found");
    }

    public EstimateProductNotFoundException(String message) {
      super(message);
    }
  }

}
