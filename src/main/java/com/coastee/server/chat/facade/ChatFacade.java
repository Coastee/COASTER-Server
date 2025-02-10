package com.coastee.server.chat.facade;

import com.coastee.server.chat.dto.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.coastee.server.global.domain.Constant.CHATROOM_CHANNEL_NAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFacade {
    private final Map<String, ChannelTopic> channelTopicMap;
    private final RedisTemplate<String, Object> redisTemplate;

    public void chat(final ChatRequest chatRequest) {
        redisTemplate.convertAndSend(channelTopicMap.get(CHATROOM_CHANNEL_NAME).getTopic(), chatRequest);
    }
}
