package nnt.com.domain.homestay.repository;

import nnt.com.domain.homestay.model.entity.Homestay;
import org.springframework.data.domain.Page;

public interface HomestayDomainRepository {
    Page<Homestay> findAll(int page, int size, String sortBy, String direction);

    Homestay findById(Long homestayId);

    Homestay save(Homestay homestay);

    void deleteById(Long homestayId);

    Homestay update(Homestay homestay);
}
