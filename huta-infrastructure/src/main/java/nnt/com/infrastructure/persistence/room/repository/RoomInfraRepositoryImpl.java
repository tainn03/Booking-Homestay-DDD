package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.aggregates.repository.RoomDomainRepository;
import nnt.com.infrastructure.persistence.room.database.jpa.RoomInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomInfraRepositoryImpl implements RoomDomainRepository {
    RoomInfraRepositoryJpa roomInfraRepositoryJpa;

    @Override
    public Room save(Room room) {
        return roomInfraRepositoryJpa.save(room);
    }

    @Override
    public Room update(Room room) {
        return roomInfraRepositoryJpa.save(room);
    }

    @Override
    public Room getById(Long id) {
        return roomInfraRepositoryJpa.findById(id).orElse(null);
    }

    @Override
    public Page<Room> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return roomInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        roomInfraRepositoryJpa.deleteById(id);
    }
}
