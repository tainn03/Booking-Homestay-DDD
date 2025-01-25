package nnt.com.application.service.user;

import nnt.com.domain.aggregates.model.dto.response.UserResponse;

public interface UserAppService {
    UserResponse getProfile();
}
