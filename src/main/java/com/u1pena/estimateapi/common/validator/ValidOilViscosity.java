package com.u1pena.estimateapi.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = OilViscosityValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOilViscosity {

  String message() default "オイルの粘度は正しい名前を入力してください。例）0w-20,5w-30等";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
