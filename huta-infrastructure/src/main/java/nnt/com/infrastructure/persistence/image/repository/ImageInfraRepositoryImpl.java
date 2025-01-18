package nnt.com.infrastructure.persistence.image.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.domain.aggregates.image.model.entity.Image;
import nnt.com.domain.aggregates.image.repository.ImageDomainRepository;
import nnt.com.infrastructure.persistence.image.database.jpa.ImageInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageInfraRepositoryImpl implements ImageDomainRepository {
    ImageInfraRepositoryJpa imageInfraRepositoryJpa;

    @Override
    public Image save(Image image) {
        return imageInfraRepositoryJpa.save(image);
    }

    @Override
    public Image update(Image image) {
        return imageInfraRepositoryJpa.save(image);
    }

    @Override
    public Image getById(String url) {
        return imageInfraRepositoryJpa.findByUrl(url)
                .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));
    }

    @Override
    public Page<Image> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return imageInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(String url) {
        imageInfraRepositoryJpa.deleteByUrl(url);
    }
}
