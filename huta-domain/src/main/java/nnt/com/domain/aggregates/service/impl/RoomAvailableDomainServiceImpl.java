package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.RoomAvailable;
import nnt.com.domain.aggregates.repository.RoomAvailableDomainRepository;
import nnt.com.domain.aggregates.service.RoomAvailableDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomAvailableDomainServiceImpl implements RoomAvailableDomainService {
    RoomAvailableDomainRepository roomAvailableDomainRepository;

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomAvailableDomainRepository.isRoomAvailable(roomId, checkIn, checkOut);
    }

    @Override
    public List<LocalDate> getUnavailableDates(Long id, LocalDate of, LocalDate of1) {
        return roomAvailableDomainRepository.getUnavailableDates(id, of, of1);
    }

    @Override
    public RoomAvailable save(RoomAvailable roomAvailable) {
        return roomAvailableDomainRepository.save(roomAvailable);
    }

    @Override
    public RoomAvailable update(RoomAvailable roomAvailable) {
        return roomAvailableDomainRepository.update(roomAvailable);
    }

    @Override
    public RoomAvailable getById(Long id) {
        return roomAvailableDomainRepository.getById(id);
    }

    @Override
    public Page<RoomAvailable> getAll(int page, int size, String sort, String direction) {
        return roomAvailableDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        roomAvailableDomainRepository.delete(id);
    }
}
