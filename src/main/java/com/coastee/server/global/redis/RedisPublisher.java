package com.coastee.server.global.redis;

import com.coastee.server.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public void publish(final ChatMessage message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
