package nnt.com.domain.aggregates.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.homestay.repository.TypeHomestayDomainRepository;
import nnt.com.domain.aggregates.homestay.model.entity.TypeHomestay;
import nnt.com.domain.aggregates.homestay.service.TypeHomestayDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TypeHomestayDomainServiceImpl implements TypeHomestayDomainService {
    TypeHomestayDomainRepository typeHomestayDomainRepository;

    @Override
    public TypeHomestay save(TypeHomestay typeHomestay) {
        return typeHomestayDomainRepository.save(typeHomestay);
    }

    @Override
    public TypeHomestay update(TypeHomestay typeHomestay) {
        return typeHomestayDomainRepository.save(typeHomestay);
    }

    @Override
    public TypeHomestay getById(String id) {
        return typeHomestayDomainRepository.getById(id);
    }

    @Override
    public Page<TypeHomestay> getAll(int page, int size, String sort, String direction) {
        return typeHomestayDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(String id) {
        typeHomestayDomainRepository.delete(id);
    }
}
