package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HomestayMapper {
    @Mapping(target = "homestayName", source = "name")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "rules", ignore = true)
    @Mapping(target = "typeHomestay", ignore = true)
    Homestay toEntity(HomestayRequest request);

    @Mapping(target = "title", source = "homestayName")
    @Mapping(target = "typeHomestay", source = "typeHomestay.name")
    @Mapping(target = "authorId", source = "owner.id")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "address", source = "addressDetail")
    HomestayResponse toDTO(Homestay homestay);

    @Mapping(target = "homestayName", source = "name")
    @Mapping(target = "typeHomestay", ignore = true)
    Homestay update(@MappingTarget Homestay homestay, HomestayRequest request);
}
