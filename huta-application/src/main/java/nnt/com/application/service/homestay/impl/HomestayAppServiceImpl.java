package nnt.com.application.service.homestay.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.model.dto.request.HomestayRequest;
import nnt.com.application.model.dto.response.HomestayResponse;
import nnt.com.application.model.mapper.HomestayMapper;
import nnt.com.application.model.mapper.HomestaySearchMapper;
import nnt.com.application.service.homestay.HomestayAppService;
import nnt.com.domain.homestay.model.document.HomestayDocument;
import nnt.com.domain.homestay.model.entity.Homestay;
import nnt.com.domain.homestay.service.HomestayDomainService;
import nnt.com.domain.homestay.service.HomestaySearchDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomestayAppServiceImpl implements HomestayAppService {
    HomestayDomainService homestayDomainService;
    HomestayMapper homestayMapper;
    HomestaySearchDomainService homestaySearchDomainService;
    HomestaySearchMapper homestaySearchMapper;

    @Override
    public Page<HomestayResponse> getAll(int page, int size, String sortBy, String direction) {
        Page<Homestay> homestays = homestayDomainService.getAll(page, size, sortBy, direction);
        return homestays.map(homestayMapper::toDTO);
    }

    @Override
    public HomestayResponse getHomestayById(Long homestayId) {
        return homestayMapper.toDTO(homestayDomainService.getById(homestayId));
    }

    @Override
    public HomestayResponse save(HomestayRequest request) {
        Homestay homestay = homestayDomainService.save(homestayMapper.toEntity(request));
        saveHomestaySearch(request, homestay);
        return homestayMapper.toDTO(homestay);
    }

    @Override
    public void deleteById(Long homestayId) {
        homestayDomainService.delete(homestayId);
        homestaySearchDomainService.deleteById(homestayId);
    }

    @Override
    public HomestayResponse update(Long homestayId, HomestayRequest request) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        updateHomestaySearch(request, homestay);
        Homestay updatedHomestay = homestayMapper.updateEntity(homestay, request);
        return homestayMapper.toDTO(homestayDomainService.update(updatedHomestay));
    }

    private void updateHomestaySearch(HomestayRequest request, Homestay homestay) {
        homestaySearchDomainService.deleteById(homestay.getId());
        saveHomestaySearch(request, homestay);
    }

    private void saveHomestaySearch(HomestayRequest request, Homestay homestay) {
        HomestayDocument homestaySearch = homestaySearchMapper.toDocument(request);
        homestaySearch.setId(homestay.getId().toString());
        homestaySearchDomainService.save(homestaySearch);
    }
}
