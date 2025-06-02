package example.maintenance.estimate.customer.dto.request.validator;

import example.maintenance.estimate.customer.entity.enums.OilViscosity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OilViscosityValidator implements ConstraintValidator<ValidOilViscosity, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false; // 空の値は許可しない
    }
    return OilViscosity.isValid(value); // OilViscosityのEnumに含まれているかどうか
  }
}

