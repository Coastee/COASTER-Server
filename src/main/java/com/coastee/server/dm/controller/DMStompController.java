package com.coastee.server.dm.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.dto.request.DMRequest;
import com.coastee.server.dm.facade.DMFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DMStompController {
    private final DMFacade dmFacade;

    @MessageMapping("/dm")
    public void message(
            @Auth final Accessor accessor,
            final DMRequest dmRequest
    ) {
        log.info("==pub== " + dmRequest.getContent());
        dmFacade.message(accessor, dmRequest);
    }
}
