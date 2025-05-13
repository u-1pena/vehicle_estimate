package example.maintenance.estimate.customer.dto.request.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PrefectureValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrefecture {

  String message() default "都道府県は正しい名前を入力してください";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
