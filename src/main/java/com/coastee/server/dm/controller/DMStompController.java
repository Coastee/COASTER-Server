package com.coastee.server.dm.controller;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.dto.request.DMRequest;
import com.coastee.server.dm.facade.DMFacade;
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
    public void message(
            final Authentication authentication,
            final DMRequest dmRequest
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        dmFacade.message(accessor, dmRequest);
    }

    @MessageMapping("/dms/{room-id}")
    public void message(
            final Authentication authentication,
            @DestinationVariable("room-id") final Long roomId,
            final DMRequest dmRequest
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        dmFacade.message(accessor, roomId, dmRequest);
    }

    @MessageMapping("/dms/{room-id}/{dm-id}/delete")
    public void deleteMessage(
            final Authentication authentication,
            @DestinationVariable("room-id") final Long roomId,
            @DestinationVariable("dm-id") final Long dmId
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        dmFacade.deleteMessage(accessor, roomId, dmId);
    }
}
