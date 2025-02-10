package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.request.RatingRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.shared.behaviors.BaseBehaviors;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HomestayDomainService extends BaseBehaviors<Homestay, Long> {
    HomestayResponse save(HomestayRequest request);

    Page<HomestayResponse> getAllHomestay(int page, int size, String sortBy, String direction);

    HomestayResponse getHomestayById(long homestayId);

    HomestayResponse updateHomestay(long homestayId, HomestayRequest request);

    List<HomestayResponse> getByOwner(Long id);

    List<HomestayResponse> getWishlist(User currentUser);

    HomestayResponse ratingHomestay(Homestay homestay, User user, RatingRequest request);
}
