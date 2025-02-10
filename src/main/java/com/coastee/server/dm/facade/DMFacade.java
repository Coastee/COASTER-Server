package com.coastee.server.dm.facade;

import com.coastee.server.dm.dto.request.DMRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.coastee.server.global.domain.Constant.DM_CHANNEL_NAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DMFacade {
    private final Map<String, ChannelTopic> channelTopicMap;
    private final RedisTemplate<String, Object> redisTemplate;

    public void message(final DMRequest dmRequest) {
        redisTemplate.convertAndSend(channelTopicMap.get(DM_CHANNEL_NAME).getTopic(), dmRequest);
    }
}
