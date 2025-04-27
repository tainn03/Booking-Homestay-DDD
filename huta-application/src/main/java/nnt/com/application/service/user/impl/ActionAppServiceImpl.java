package nnt.com.application.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.ActionAppService;
import nnt.com.domain.aggregates.model.document.Action;
import nnt.com.domain.aggregates.model.dto.response.LoginStatByYearResponse;
import nnt.com.domain.aggregates.service.ActionSearchDomainService;
import nnt.com.domain.shared.model.vo.UserAction;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ActionAppServiceImpl implements ActionAppService {
    ActionSearchDomainService actionSearchDomainService;
    RedisCache redisCache;

    @Override
    public List<Action> getAll() {
        List cachedActions = getActionsFromCache();
        if (cachedActions != null) {
            return cachedActions;
        }
        List<Action> actions = actionSearchDomainService.getAll();
        setActionsToCache(actions);

        return actions;
    }

    private List getActionsFromCache() {
        return redisCache.getObject("actions", List.class);
    }

    private void setActionsToCache(List<Action> actions) {
        redisCache.setObject("actions", actions, 1L, TimeUnit.MINUTES);
    }

    @Override
    public LoginStatByYearResponse getLoginStatByYear(String from, String to) {
        LoginStatByYearResponse cachedLoginStat = getLoginStatFromCache(from, to);
        if (cachedLoginStat != null) {
            return cachedLoginStat;
        }

        LocalDate fromDate = getDate(from);
        LocalDate toDate = getDate(to);

        List<Action> actions = getActionFromDateToDate(fromDate, toDate, UserAction.LOGIN);
        long numberOfSuccess = actions.stream()
                .filter(Action::isSuccess)
                .count();
        long numberOfFail = actions.size() - numberOfSuccess;

        LoginStatByYearResponse loginStat = LoginStatByYearResponse.builder()
                .successCount(numberOfSuccess)
                .failCount(numberOfFail)
                .build();
        setLoginStatToCache(loginStat);
        return loginStat;
    }

    private List<Action> getActionFromDateToDate(LocalDate fromDate, LocalDate toDate, UserAction type) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return actionSearchDomainService.getAll().stream()
                .filter(action -> action.getAction().equals(type.toString()))
                .filter(action -> LocalDate.parse(action.getTimestamp(), formatter).isAfter(ChronoLocalDate.from(fromDate.atStartOfDay())))
                .filter(action -> LocalDate.parse(action.getTimestamp(), formatter).isBefore(ChronoLocalDate.from(toDate.plusDays(1).atStartOfDay())))
                .toList();
    }

    private LocalDate getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(date, formatter);
    }

    private LoginStatByYearResponse getLoginStatFromCache(String from, String to) {
        return redisCache.getObject("loginStatByYear:" + from + ":" + to
                , LoginStatByYearResponse.class);
    }

    private void setLoginStatToCache(LoginStatByYearResponse loginStat) {
        redisCache.setObject("loginStatByYear", loginStat, 1L, TimeUnit.MINUTES);
    }

    @Override
    public Action getById(String id) {
        return actionSearchDomainService.getById(id);
    }

    @Override
    public void delete(String id) {
        actionSearchDomainService.delete(id);
    }
}
