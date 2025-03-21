package com.coastee.server.auth.infrastructurre;

import com.coastee.server.login.infrastructure.JwtHeaderUtil;
import com.coastee.server.login.infrastructure.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.coastee.server.global.domain.Constant.HEADER_AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompAuthorizationHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    private final AuthProvider authProvider;

    @Override
    public Message<?> preSend(
            final Message<?> message,
            final MessageChannel channel
    ) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            String accessToken = JwtHeaderUtil.getToken(HEADER_AUTHORIZATION, accessor);
            jwtProvider.validateAccessToken(accessToken);
            Authentication authentication = authProvider.getAuthentication(accessToken);
            accessor.setUser(authentication);
        }
        return message;
    }
}
