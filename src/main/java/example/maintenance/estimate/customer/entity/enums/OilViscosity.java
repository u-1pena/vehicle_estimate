package example.maintenance.estimate.customer.entity.enums;

public enum OilViscosity {
  _0W8("0w-8"),
  _0W16("0w-16"),
  _0W20("0w-20"),
  _5W30("5w-30"),
  _5W40("5w-40"),
  _10W30("10w-30"),
  _0W50("0w-50");

  private final String label;

  OilViscosity(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static boolean isValid(String value) {
    for (OilViscosity viscosity : values()) {
      if (viscosity.getLabel().equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }
}
