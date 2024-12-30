package nnt.com.domain.homestay.service;

import nnt.com.domain.homestay.model.entity.Homestay;
import org.springframework.data.domain.Page;

public interface HomestayDomainService {
    Page<Homestay> findAll(int page, int size, String sortBy, String direction);

    Homestay findById(Long homestayId);

    Homestay save(Homestay homestay);

    void deleteById(Long homestayId);

    Homestay update(Homestay homestay);
}
