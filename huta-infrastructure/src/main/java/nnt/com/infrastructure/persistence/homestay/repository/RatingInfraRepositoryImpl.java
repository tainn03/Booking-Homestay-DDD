package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Review;
import nnt.com.domain.aggregates.repository.RatingDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.homestay.database.jpa.RatingInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RatingInfraRepositoryImpl implements RatingDomainRepository {
    RatingInfraRepositoryJpa ratingInfraRepositoryJpa;

    @Override
    public Review save(Review rating) {
        return ratingInfraRepositoryJpa.save(rating);
    }

    @Override
    public Review update(Review rating) {
        return ratingInfraRepositoryJpa.save(rating);
    }

    @Override
    public Review getById(Long id) {
        return ratingInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.RATING_NOT_FOUND));
    }

    @Override
    public Page<Review> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return ratingInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        ratingInfraRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Review> findAll() {
        return ratingInfraRepositoryJpa.findAll();
    }
}
