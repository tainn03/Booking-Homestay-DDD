package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.Action;
import nnt.com.domain.aggregates.repository.ActionSearchDomainRepository;
import nnt.com.infrastructure.persistence.user.database.elastic.ActionInfraRepositoryElastic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ActionInfraRepositoryImpl implements ActionSearchDomainRepository {
    ActionInfraRepositoryElastic actionInfraRepositoryElastic;

    @Override
    public Action save(Action action) {
        return actionInfraRepositoryElastic.save(action);
    }

    @Override
    public List<Action> getAll() {
        return StreamSupport.stream(actionInfraRepositoryElastic.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Action getById(String id) {
        return actionInfraRepositoryElastic.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {
        actionInfraRepositoryElastic.deleteById(id);
    }
}
