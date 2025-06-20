package com.u1pena.estimateapi.common.validator;

import com.u1pena.estimateapi.common.enums.CarWashSize;
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

