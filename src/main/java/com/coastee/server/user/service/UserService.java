package com.coastee.server.user.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import com.coastee.server.user.dto.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_REFRESH_TOKEN;
import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_USER_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(INVALID_USER_ID));
    }

    public User findByRefreshToken(final String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidJwtException(INVALID_REFRESH_TOKEN));
    }

    @Transactional
    public User findOrCreateUser(final OAuthUserInfo userInfo) {
        return userRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> createAndSaveUser(userInfo));
    }

    private User createAndSaveUser(final OAuthUserInfo userInfo) {
        User user = new User(
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getSocialType(),
                userInfo.getSocialId()
        );
        return userRepository.save(user);
    }

    public void update(final User user, final UserUpdateRequest request) {
        request.validateNullValue(user);
        user.updateNickname(request.getNickname());
        user.updateUrlList(request.getUrlList());
        user.updateUserIntro(request.getHeadline(), request.getJob(), request.getExpYears());
        user.updateBio(request.getBio());
    }
}
