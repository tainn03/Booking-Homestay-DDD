package nnt.com.application.service.homestay.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.homestay.HomestayAppService;
import nnt.com.application.service.homestay.cache.HomestayAppServiceCache;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomestayAppServiceImpl implements HomestayAppService {
    HomestayAppServiceCache homestayAppServiceCache;
    HomestaySearchDomainService homestaySearchDomainService;

    @Override
    public Page<HomestayResponse> getAll(int page, int size, String sortBy, String direction) {
        return homestayAppServiceCache.getAll(page, size, sortBy, direction);
    }

    @Override
    public HomestayResponse getHomestayById(Long homestayId) {
        return homestayAppServiceCache.getHomestayById(homestayId);
    }

    @Override
    public HomestayResponse save(HomestayRequest request) {
        HomestayResponse response = homestayAppServiceCache.save(request);
        return saveHomestaySearch(request, response);
    }

    @Override
    public void deleteById(Long homestayId) {
        homestayAppServiceCache.deleteById(homestayId);
        homestaySearchDomainService.deleteById(homestayId);
    }

    @Override
    public HomestayResponse update(Long homestayId, HomestayRequest request) {
        HomestayResponse response = homestayAppServiceCache.update(homestayId, request);
        return updateHomestaySearch(request, response);
    }

    private HomestayResponse updateHomestaySearch(HomestayRequest request, HomestayResponse response) {
        homestaySearchDomainService.deleteById(response.getId());
        return saveHomestaySearch(request, response);
    }

    private HomestayResponse saveHomestaySearch(HomestayRequest request, HomestayResponse response) {
        homestaySearchDomainService.save(request, response);
        return response;
    }
}
