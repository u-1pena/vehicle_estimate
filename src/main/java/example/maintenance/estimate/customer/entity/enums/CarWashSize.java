package example.maintenance.estimate.customer.entity.enums;

public enum CarWashSize {
  SS("軽自動車"),
  S("軽自動車・小型車"),
  M("小型車・普通車"),
  L("普通車・大型車"),
  LL("大型車"),
  XL("特大車");

  private final String description;

  CarWashSize(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static boolean isValid(String value) {
    for (CarWashSize carWashSize : values()) {
      if (carWashSize.name().equals(value)) {
        return true;
      }
    }
    return false;
  }

}
