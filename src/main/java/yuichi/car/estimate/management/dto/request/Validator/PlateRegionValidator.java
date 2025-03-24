package yuichi.car.estimate.management.dto.request.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import yuichi.car.estimate.management.entity.enums.PlateRegion;

public class PlateRegionValidator implements ConstraintValidator<ValidPlateRegion, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return false;// nullまたは空文字の場合はfalse
    }
    return PlateRegion.isValid(value); // PlateRegionのEnumに含まれているかどうか
  }
}
