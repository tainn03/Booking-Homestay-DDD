package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import nnt.com.domain.aggregates.model.dto.response.RoomResponse;
import nnt.com.domain.aggregates.model.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "homestayId", source = "homestay.id")
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "id", source = "id")
    RoomResponse toDTO(Room room);

    @Mapping(target = "homestay", ignore = true)
    Room toEntity(RoomRequest roomRequest);
}
