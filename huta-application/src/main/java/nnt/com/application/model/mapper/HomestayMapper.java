package nnt.com.application.model.mapper;

import nnt.com.application.model.dto.request.HomestayRequest;
import nnt.com.application.model.dto.response.HomestayResponse;
import nnt.com.domain.homestay.model.entity.Homestay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HomestayMapper {
    HomestayResponse toDTO(Homestay homestay);

    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    Homestay toEntity(HomestayRequest homestayResponse);

    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    Homestay updateEntity(@MappingTarget Homestay homestay, HomestayRequest homestayRequest);
}
