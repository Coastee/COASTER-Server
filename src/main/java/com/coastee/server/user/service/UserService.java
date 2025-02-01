package com.coastee.server.user.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
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
                .orElseGet(() -> newUser(userInfo));
    }

    private User newUser(final OAuthUserInfo userInfo) {
        User user = User.of()
                .nickname(userInfo.getName())
                .email(userInfo.getEmail())
                .socialType(userInfo.getSocialType())
                .socialId(userInfo.getSocialId())
                .build();
        return userRepository.save(user);
    }
}
