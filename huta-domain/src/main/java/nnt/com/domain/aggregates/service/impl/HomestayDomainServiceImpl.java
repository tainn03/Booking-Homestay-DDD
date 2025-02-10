package nnt.com.domain.aggregates.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.request.RatingRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.*;
import nnt.com.domain.aggregates.model.mapper.HomestayMapper;
import nnt.com.domain.aggregates.repository.HomestayDomainRepository;
import nnt.com.domain.aggregates.repository.TypeHomestayDomainRepository;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HomestayDomainServiceImpl implements HomestayDomainService {
    HomestayMapper homestayMapper;
    UserDomainService userDomainService;
    HomestayDomainRepository homestayDomainRepository;
    TypeHomestayDomainRepository typeHomestayDomainRepository;

    @Override
    public Page<Homestay> getAll(int page, int size, String sortBy, String direction) {
        return homestayDomainRepository.getAll(page, size, sortBy, direction);
    }

    @Override
    public Homestay getById(Long homestayId) {
        return homestayDomainRepository.getById(homestayId);
    }

    @Override
    public Homestay save(Homestay homestay) {
        return homestayDomainRepository.save(homestay);
    }

    @Override
    public void delete(Long homestayId) {
        homestayDomainRepository.delete(homestayId);
    }

    @Override
    public Homestay update(Homestay homestay) {
        return homestayDomainRepository.update(homestay);
    }

    @Override
    public HomestayResponse save(HomestayRequest request) {
        Homestay homestay = homestayMapper.toEntity(request);

        homestay.setOwner(request.getEmailOwner() != null ?
                userDomainService.getByEmail(request.getEmailOwner()) : (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        homestay.setRules(request.getRules().stream().map(ruleRequest -> Rule.builder()
                .name(ruleRequest.getName())
                .value(ruleRequest.getValue())
                .homestay(homestay)
                .build()
        ).toList());
        homestay.setTags(request.getTags().stream().map(tagRequest -> Tag.builder()
                .tag(tagRequest.getTag())
                .homestay(homestay)
                .build()
        ).toList());
        homestay.setAddressDetail(request.getAddressDetail() + ", " + request.getWard() + ", " + request.getDistrict() + ", " + request.getCity());
        try {
            homestay.setTypeHomestay(typeHomestayDomainRepository.getById(request.getTypeHomestay()));
        } catch (Exception e) {
            homestay.setTypeHomestay(TypeHomestay.builder()
                    .name(request.getTypeHomestay())
                    .build());
            typeHomestayDomainRepository.save(homestay.getTypeHomestay());
        }
        return convertToResponse(save(homestay));
    }

    @Override
    public Page<HomestayResponse> getAllHomestay(int page, int size, String sortBy, String direction) {
        Page<Homestay> homestays = getAll(page, size, sortBy, direction);
        return homestays.map(this::convertToResponse);
    }

    @Override
    public HomestayResponse getHomestayById(long homestayId) {
        return convertToResponse(getById(homestayId));
    }

    @Override
    public HomestayResponse updateHomestay(long homestayId, HomestayRequest request) {
        return null;
    }

    @Override
    public List<HomestayResponse> getByOwner(Long id) {
        List<Homestay> homestays = homestayDomainRepository.getByOwner(id);
        return homestays.stream().map(this::convertToResponse).toList();
    }

    @Override
    public List<HomestayResponse> getWishlist(User currentUser) {
        List<Homestay> homestays = currentUser.getWishlist();
        return homestays.stream().map(this::convertToResponse).toList();
    }

    @Override
    public HomestayResponse ratingHomestay(Homestay homestay, User user, RatingRequest request) {
        homestay.getReviews().add(Review.builder()
                .user(user)
                .homestay(homestay)
                .rating(request.getRating())
                .comment(request.getComment())
                .build());
        return convertToResponse(update(homestay));
    }

    private HomestayResponse convertToResponse(Homestay homestay) {
        HomestayResponse response = homestayMapper.toDTO(homestay);
        if (homestay.getReviews() != null && !homestay.getReviews().isEmpty()) {
            double rating = homestay.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0);
            response.setRating(rating);
        }
        return response;
    }

}
