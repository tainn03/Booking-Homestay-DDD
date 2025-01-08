package nnt.com.application.model.mapper;

import nnt.com.application.model.dto.request.HomestayRequest;
import nnt.com.domain.homestay.model.document.HomestayDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomestaySearchMapper {
    HomestayDocument toDocument(HomestayRequest request);
}
