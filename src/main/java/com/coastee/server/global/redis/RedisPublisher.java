package com.coastee.server.global.redis;

import com.coastee.server.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(
            final ChannelTopic topic,
            final ChatMessage chatMessage
    ) {
        redisTemplate.convertAndSend(topic.getTopic(), chatMessage);
    }
}
