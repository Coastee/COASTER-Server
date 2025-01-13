package com.coastee.server.user.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import com.coastee.server.user.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_USER_ID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ProfileResponse getProfile(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(INVALID_USER_ID));
        return ProfileResponse.from(user);
    }
}
