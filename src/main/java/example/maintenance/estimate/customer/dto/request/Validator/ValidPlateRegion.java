package example.maintenance.estimate.customer.dto.request.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PlateRegionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlateRegion {

  String message() default "ナンバープレートの地域名が不正です";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
