package nnt.com.application.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import nnt.com.application.model.annotation.validator.EnumPatternValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, METHOD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = EnumPatternValidator.class)
public @interface EnumPattern {
    String name();

    String regexp();

    String message() default "{name} phải khớp với mẫu {regexp}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}