package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.room.model.entity.Room;
import nnt.com.domain.room.repository.RoomDomainRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomInfraRepositoryImpl implements RoomDomainRepository {
    @Override
    public Room save(Room room) {
        return null;
    }

    @Override
    public Room update(Room room) {
        return null;
    }

    @Override
    public Room getById(Long id) {
        return null;
    }

    @Override
    public Page<Room> getAll(int page, int size, String sort, String direction) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
