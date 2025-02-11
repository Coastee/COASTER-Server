package com.coastee.server.dm.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.dm.dto.request.DMRequest;
import com.coastee.server.dm.facade.DMFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DMStompController {
    private final DMFacade dmFacade;

    @MessageMapping("/dm")
    public void message(
            final DMRequest dmRequest,
            @Header("Authorization") String authorization
    ) {
        log.info("==pub== " + dmRequest.getContent());
        // TODO: 여기서 가져오질 못함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        dmFacade.message(null, dmRequest);
    }
}
