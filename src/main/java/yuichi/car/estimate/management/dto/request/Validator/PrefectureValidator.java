package yuichi.car.estimate.management.dto.request.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import yuichi.car.estimate.management.entity.enums.Prefecture;

public class PrefectureValidator implements ConstraintValidator<ValidPrefecture, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false; // 空の値は許可しない
    }
    return Prefecture.isValid(value); // PrefectureのEnumに含まれているかどうか
  }
}

