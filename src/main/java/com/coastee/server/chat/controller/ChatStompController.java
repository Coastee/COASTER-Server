package com.coastee.server.chat.controller;

import com.coastee.server.chat.dto.request.ChatRequest;
import com.coastee.server.chat.facade.ChatFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatFacade chatFacade;

    @MessageMapping("/chat")
    public void message(
//            @Auth final Accessor accessor,
            final ChatRequest chatRequest
    ) {
        log.info("==pub== " + chatRequest.getContent());
        chatFacade.chat(chatRequest);
    }
}
