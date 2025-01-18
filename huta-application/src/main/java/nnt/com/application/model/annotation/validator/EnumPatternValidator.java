package nnt.com.application.model.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.model.annotation.EnumPattern;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class EnumPatternValidator implements ConstraintValidator<EnumPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumPattern enumPattern) {
        try {
            pattern = Pattern.compile(enumPattern.regexp());
        } catch (PatternSyntaxException e) {
            throw new BusinessException(ErrorCode.INVALID_ENUM_PATTERN);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        Matcher m = pattern.matcher(value.name());
        return m.matches();
    }
}
