package com.coastee.server.chat.controller;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.dto.request.ChatRequest;
import com.coastee.server.chat.facade.ChatFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatStompController {
    private final ChatFacade chatFacade;

    @MessageMapping("/chats/{roomId}")
    public void chat(
            final Authentication authentication,
            @DestinationVariable("roomId") final Long roomId,
            final ChatRequest chatRequest
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        chatFacade.chat(accessor, roomId, chatRequest);
    }

    @MessageMapping("/chats/{roomId}/{chatId}/delete")
    public void deleteChat(
            final Authentication authentication,
            @DestinationVariable("roomId") final Long roomId,
            @DestinationVariable("chatId") final Long chatId
    ) {
        Accessor accessor = Accessor.user(Long.parseLong(authentication.getPrincipal().toString()));
        chatFacade.deleteChat(accessor, roomId, chatId);
    }
}
