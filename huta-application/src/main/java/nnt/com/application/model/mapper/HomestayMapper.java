package nnt.com.application.model.mapper;

import nnt.com.application.model.dto.request.HomestayRequest;
import nnt.com.application.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.homestay.model.entity.Homestay;
import nnt.com.domain.aggregates.image.model.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HomestayMapper {
    //    //    Integer numLike;
    //    //    List<Room> rooms;
    ////    List<Rating> reviews;
    @Mapping(target = "emailOwner", source = "owner.email")
    @Mapping(target = "imageUrls", source = "images", qualifiedByName = "imagesToUrls")
    @Mapping(target = "typeHomestay", source = "typeHomestay.name")
    @Mapping(target = "district", source = "district.name")
    @Mapping(target = "name", source = "homestayName")
    HomestayResponse toDTO(Homestay homestay);

    @Mapping(target = "district", ignore = true)
    @Mapping(target = "typeHomestay", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "homestayName", source = "name")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    Homestay toEntity(HomestayRequest homestayRequest);

    @Mapping(target = "district", ignore = true)
    @Mapping(target = "typeHomestay", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "homestayName", source = "name")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "id", ignore = true)
    Homestay updateEntity(@MappingTarget Homestay homestay, HomestayRequest homestayRequest);

    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream().map(Image::getUrl).toList();
    }
}
