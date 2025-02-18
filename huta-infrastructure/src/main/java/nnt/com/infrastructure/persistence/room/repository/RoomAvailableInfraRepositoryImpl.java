package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.RoomAvailable;
import nnt.com.domain.aggregates.repository.RoomAvailableDomainRepository;
import nnt.com.infrastructure.persistence.room.database.jpa.RoomAvailableRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomAvailableInfraRepositoryImpl implements RoomAvailableDomainRepository {
    RoomAvailableRepositoryJpa roomAvailableRepositoryJpa;

    @Override
    public RoomAvailable save(RoomAvailable roomAvailable) {
        return roomAvailableRepositoryJpa.save(roomAvailable);
    }

    @Override
    public RoomAvailable update(RoomAvailable roomAvailable) {
        return roomAvailableRepositoryJpa.save(roomAvailable);
    }

    @Override
    public RoomAvailable getById(Long id) {
        return roomAvailableRepositoryJpa.findById(id).orElse(null);
    }

    @Override
    public Page<RoomAvailable> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return roomAvailableRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        roomAvailableRepositoryJpa.deleteById(id);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomAvailableRepositoryJpa.isRoomAvailable(roomId, checkIn, checkOut);
    }
}
