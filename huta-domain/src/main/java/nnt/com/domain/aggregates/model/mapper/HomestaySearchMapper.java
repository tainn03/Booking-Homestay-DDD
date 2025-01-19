package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomestaySearchMapper {
    HomestayDocument toDocument(HomestayRequest request);
}
