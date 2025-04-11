package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "roomIds", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "checkIn", ignore = true)
    @Mapping(target = "checkOut", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "user", ignore = true)
    BookingResponse toDTO(Booking booking);
}
