package com.coastee.server.dm.controller;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.dto.request.DMRequest;
import com.coastee.server.dm.facade.DMFacade;
import com.coastee.server.global.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DMStompController {
    private final DMFacade dmFacade;

    @MessageMapping("/dms/create")
    public ApiResponse<Void> message(
            final DMRequest dmRequest,
            final Authentication authentication
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        dmFacade.message(accessor, dmRequest);
        return ApiResponse.onSuccess();
    }

    @MessageMapping("/dms/{room-id}")
    public ApiResponse<Void> message(
            @DestinationVariable("room-id") final Long roomId,
            final DMRequest dmRequest,
            final Authentication authentication
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        dmFacade.message(accessor, roomId, dmRequest);
        return ApiResponse.onSuccess();
    }
}
