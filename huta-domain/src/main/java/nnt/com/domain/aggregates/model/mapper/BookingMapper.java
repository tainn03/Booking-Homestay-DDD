package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "roomNames", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "checkIn", ignore = true)
    @Mapping(target = "checkOut", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    BookingResponse toDTO(Booking booking);
}
