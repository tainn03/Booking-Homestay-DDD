package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.document.Action;

import java.util.List;

public interface ActionSearchDomainService {
    Action save(Action action);

    List<Action> getAll();

    Action getById(String id);

    void delete(String id);
}
