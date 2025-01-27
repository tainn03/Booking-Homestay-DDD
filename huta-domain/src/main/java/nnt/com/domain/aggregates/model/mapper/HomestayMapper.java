package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomestayMapper {
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "homestayName", source = "name")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "rules", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "typeHomestay", ignore = true)
    Homestay toEntity(HomestayRequest request);

    @Mapping(target = "name", source = "homestayName")
    @Mapping(target = "typeHomestay", source = "typeHomestay.name")
    @Mapping(target = "emailOwner", source = "owner.email")
    HomestayResponse toDTO(Homestay homestay);
}
