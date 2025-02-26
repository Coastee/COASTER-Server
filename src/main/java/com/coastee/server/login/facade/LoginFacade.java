package com.coastee.server.login.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.global.apipayload.exception.handler.InvalidJwtException;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.login.domain.OAuthUserInfo;
import com.coastee.server.login.dto.request.SignupRequest;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.coastee.server.login.infrastructure.loginparams.LinkedInLoginParams;
import com.coastee.server.login.service.OAuthInfoService;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.service.ServerEntryService;
import com.coastee.server.server.service.ServerService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_VALIDATE_TOKEN;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginFacade {
    private final OAuthInfoService oAuthInfoService;
    private final UserService userService;
    private final ServerService serverService;
    private final ServerEntryService serverEntryService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomEntryService chatRoomEntryService;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthTokens login(final OAuthLoginParams params) {
        OAuthUserInfo userInfo = oAuthInfoService.request(params);
        User user = userService.findOrCreateUser(userInfo);
        AuthTokens tokens = jwtProvider.createTokens(user.getId().toString());
        user.updateRefreshToken(tokens.getRefreshToken());
        return tokens;
    }

    @Transactional
    public void connect(
            final Accessor accessor,
            final LinkedInLoginParams params
    ) {
        User user = userService.findById(accessor.getUserId());
        OAuthUserInfo userInfo = oAuthInfoService.request(params);
        user.verify(userInfo.getSocialId());
    }

    public String renewalToken(final String accessToken, final String refreshToken) {
        if (jwtProvider.isValidRefreshAndExpiredAccess(refreshToken, accessToken)) {
            User user = userService.findByRefreshToken(refreshToken);
            return jwtProvider.createAccessToken(user.getId().toString());
        }
        if (jwtProvider.isValidRefreshAndValidAccess(refreshToken, accessToken)) {
            return accessToken;
        }
        throw new InvalidJwtException(FAIL_VALIDATE_TOKEN);
    }

    @Transactional
    public void signup(
            final Accessor accessor,
            final SignupRequest signupRequest
    ) {
        User user = userService.findById(accessor.getUserId());
        userService.update(user, signupRequest);
        List<Server> serverList = serverService.findAllById(signupRequest.getServerIdList());
        serverEntryService.enter(user, serverList);
        List<ChatRoom> serverChatRoomList = chatRoomService.findEntireChatRoomsByServers(serverList);
        chatRoomEntryService.enter(user, serverChatRoomList);
    }
}
