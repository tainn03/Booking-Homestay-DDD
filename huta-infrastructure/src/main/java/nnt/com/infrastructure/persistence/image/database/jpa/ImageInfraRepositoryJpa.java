package nnt.com.infrastructure.persistence.image.database.jpa;

import nnt.com.domain.aggregates.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageInfraRepositoryJpa extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);

    void deleteByUrl(String url);
}
