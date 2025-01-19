package nnt.com.application.service.homestay;

import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import org.springframework.data.domain.Page;

public interface HomestayAppService {
    Page<HomestayResponse> getAll(int page, int size, String sortBy, String dicrection);

    HomestayResponse getHomestayById(Long homestayId);

    HomestayResponse save(HomestayRequest homestay);

    void deleteById(Long homestayId);

    HomestayResponse update(Long homestayId, HomestayRequest homestay);
}
