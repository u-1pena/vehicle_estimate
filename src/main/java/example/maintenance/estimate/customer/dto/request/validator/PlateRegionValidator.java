package example.maintenance.estimate.customer.dto.request.validator;

import example.maintenance.estimate.customer.entity.enums.PlateRegion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlateRegionValidator implements ConstraintValidator<ValidPlateRegion, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return false;// nullまたは空文字の場合はfalse
    }
    return PlateRegion.isValid(value); // PlateRegionのEnumに含まれているかどうか
  }
}
