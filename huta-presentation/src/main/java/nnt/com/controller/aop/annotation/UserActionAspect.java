package nnt.com.controller.aop.annotation;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.document.Action;
import nnt.com.domain.aggregates.model.dto.request.LoginRequest;
import nnt.com.domain.aggregates.service.ActionSearchDomainService;
import nnt.com.domain.shared.model.vo.UserAction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserActionAspect {
    ActionSearchDomainService actionSearchDomainService;

    @Around("@annotation(userActionLog)")
    public Object logUserAction(ProceedingJoinPoint joinPoint, UserActionLog userActionLog) throws Throwable {
        Object result = null;
        UserAction action = userActionLog.action();
        Instant now = Instant.now();
        boolean isSuccess = true;

        Object[] args = joinPoint.getArgs();
        String email = args[0] instanceof LoginRequest ? ((LoginRequest) args[0]).getEmail()
                : SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            isSuccess = false;
            throw e;
        } finally {
            Action userAction = actionSearchDomainService.save(
                    Action.builder()
                            .action(action.toString())
                            .email(email)
                            .timestamp(now.toString())
                            .success(isSuccess)
                            .build()
            );
            log.info("{} - {} - {} - {} - {}", userAction.getId(), action, email, now, isSuccess);
        }

        return result;
    }
}