package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomestaySearchMapper {
    @Mapping(target = "location", ignore = true)
    HomestayDocument toDocument(HomestayResponse response);
}
