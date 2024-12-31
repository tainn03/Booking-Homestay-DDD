package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.homestay.model.entity.Rating;
import nnt.com.domain.homestay.repository.RatingDomainRepository;
import nnt.com.infrastructure.persistence.homestay.database.jpa.RatingInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RatingInfraRepositoryImpl implements RatingDomainRepository {
    RatingInfraRepositoryJpa ratingInfraRepositoryJpa;

    @Override
    public Rating save(Rating rating) {
        return ratingInfraRepositoryJpa.save(rating);
    }

    @Override
    public Rating update(Rating rating) {
        return ratingInfraRepositoryJpa.save(rating);
    }

    @Override
    public Rating getById(Long id) {
        return ratingInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.RATING_NOT_FOUND));
    }

    @Override
    public Page<Rating> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return ratingInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        ratingInfraRepositoryJpa.deleteById(id);
    }
}
