package nnt.com.application.service.homestay;

import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.ImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomestayAppService {
    Page<HomestayResponse> getAll(int page, int size, String sortBy, String dicrection);

    HomestayResponse getHomestayById(Long homestayId);

    HomestayResponse save(HomestayRequest homestay);

    void deleteById(Long homestayId);

    HomestayResponse update(Long homestayId, HomestayRequest homestay);

    List<ImageResponse> getHomestayImages(Long homestayId);

    List<ImageResponse> uploadHomestayImage(Long homestayId, String type, List<MultipartFile> files);

    List<HomestayResponse> getHomestayByOwner();
}
