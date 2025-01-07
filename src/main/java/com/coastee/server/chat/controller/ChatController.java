package com.coastee.server.chat.controller;

import com.coastee.server.chat.dto.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @MessageMapping("/chat/message")
    public void message(
//            @Auth final Accessor accessor,
            final ChatRequest chatRequest
    ) {
        log.info("==pub== " + chatRequest.getMessage());
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatRequest);
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable Long roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
}
