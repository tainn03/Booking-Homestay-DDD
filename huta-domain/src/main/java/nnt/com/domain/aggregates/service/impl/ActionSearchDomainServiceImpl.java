package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.Action;
import nnt.com.domain.aggregates.repository.ActionSearchDomainRepository;
import nnt.com.domain.aggregates.service.ActionSearchDomainService;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ActionSearchDomainServiceImpl implements ActionSearchDomainService {
    ActionSearchDomainRepository actionSearchDomainRepository;
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Action save(Action action) {
        return actionSearchDomainRepository.save(action);
    }

    @Override
    public List<Action> getAll() {
        return actionSearchDomainRepository.getAll();
    }

    @Override
    public Action getById(String id) {
        return actionSearchDomainRepository.getById(id);
    }

    @Override
    public void delete(String id) {
        actionSearchDomainRepository.delete(id);
    }
}
