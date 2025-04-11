package nnt.com.application.service.user;

import nnt.com.domain.aggregates.model.document.Action;
import nnt.com.domain.aggregates.model.dto.response.LoginStatByYearResponse;

import java.util.List;

public interface ActionAppService {

    List<Action> getAll();

    LoginStatByYearResponse getLoginStatByYear(String from, String to);

    Action getById(String id);

    void delete(String id);
}
