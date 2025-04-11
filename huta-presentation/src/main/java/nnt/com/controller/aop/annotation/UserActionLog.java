package nnt.com.controller.aop.annotation;

import nnt.com.domain.shared.model.vo.UserAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserActionLog {
    UserAction action();
}

