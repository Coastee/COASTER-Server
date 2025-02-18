package com.coastee.server.global.stomp.config;

import com.coastee.server.global.stomp.infrastructure.RedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

import static com.coastee.server.global.domain.Constant.*;

@Configuration
public class RedisConfig {
    private static final String CHATROOM_ADAPTER = "chatRoomListenerAdapter";
    private static final String DMROOM_ADAPTER = "dmListenerAdapter";

    @Bean
    public ChannelTopic chatRoomTopic() {
        return new ChannelTopic(CHATROOM_CHANNEL_NAME);
    }

    @Bean
    public ChannelTopic dmTopic() {
        return new ChannelTopic(DMROOM_CHANNEL_NAME);
    }

    @Bean
    public MessageListenerAdapter chatRoomListenerAdapter(final RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, CHATROOM_LISTENER_METHOD);
    }

    @Bean
    public MessageListenerAdapter dmListenerAdapter(final RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, DM_LISTENER_METHOD);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            final RedisConnectionFactory connectionFactory,
            final Map<String, MessageListenerAdapter> listenerAdapterMap,
            final Map<String, ChannelTopic> channelTopicMap
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterMap.get(CHATROOM_ADAPTER), channelTopicMap.get(CHATROOM_TOPIC_KEY));
        container.addMessageListener(listenerAdapterMap.get(DMROOM_ADAPTER), channelTopicMap.get(DMROOM_TOPIC_KEY));
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            final RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}
