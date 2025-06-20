package com.u1pena.estimateapi.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class YenAmountValidator implements ConstraintValidator<YenAmount, BigDecimal> {

  @Override
  public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return value.stripTrailingZeros().scale() <= 0 && value.compareTo(BigDecimal.ZERO) >= 0;
    // Check if the value is an integer (no decimal part)
  }
}
