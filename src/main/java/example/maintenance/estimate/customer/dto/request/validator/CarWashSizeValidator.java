package example.maintenance.estimate.customer.dto.request.validator;

import example.maintenance.estimate.customer.entity.enums.CarWashSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CarWashSizeValidator implements ConstraintValidator<ValidCarWashSize, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false; // 空の値は許可しない
    }
    return CarWashSize.isValid(value); // CarWashSizeのEnumに含まれているかどうか
  }
}

