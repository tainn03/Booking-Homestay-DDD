package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.aggregates.repository.RoomDomainRepository;
import nnt.com.domain.aggregates.service.RoomDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomDomainServiceImpl implements RoomDomainService {
    RoomDomainRepository roomDomainRepository;

    @Override
    public Room save(Room room) {
        return roomDomainRepository.save(room);
    }

    @Override
    public Room update(Room room) {
        return roomDomainRepository.update(room);
    }

    @Override
    public Room getById(Long id) {
        return roomDomainRepository.getById(id);
    }

    @Override
    public Page<Room> getAll(int page, int size, String sort, String direction) {
        return roomDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        roomDomainRepository.delete(id);
    }
}
