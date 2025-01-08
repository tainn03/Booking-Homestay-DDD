package nnt.com.domain.homestay.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.homestay.model.entity.Homestay;
import nnt.com.domain.homestay.repository.HomestayDomainRepository;
import nnt.com.domain.homestay.service.HomestayDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HomestayDomainServiceImpl implements HomestayDomainService {
    HomestayDomainRepository homestayDomainRepository;

    @Override
    public Page<Homestay> getAll(int page, int size, String sortBy, String direction) {
        return homestayDomainRepository.getAll(page, size, sortBy, direction);
    }

    @Override
    public Homestay getById(Long homestayId) {
        return homestayDomainRepository.getById(homestayId);
    }

    @Override
    public Homestay save(Homestay homestay) {
        return homestayDomainRepository.save(homestay);
    }

    @Override
    public void delete(Long homestayId) {
        homestayDomainRepository.delete(homestayId);
    }

    @Override
    public Homestay update(Homestay homestay) {
        return homestayDomainRepository.update(homestay);
    }
}
