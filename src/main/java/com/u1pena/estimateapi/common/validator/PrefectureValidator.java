package com.u1pena.estimateapi.common.validator;

import com.u1pena.estimateapi.common.enums.Prefecture;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrefectureValidator implements ConstraintValidator<ValidPrefecture, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false; // 空の値は許可しない
    }
    return Prefecture.isValid(value); // PrefectureのEnumに含まれているかどうか
  }
}

