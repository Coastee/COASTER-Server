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

    @Bean
    public ChannelTopic chatRoomTopic() {
        return new ChannelTopic(CHATROOM_CHANNEL_NAME);
    }

    @Bean
    public ChannelTopic dmTopic() {
        return new ChannelTopic(DM_CHANNEL_NAME);
    }

    @Bean
    public Map<String, ChannelTopic> topics() {
        return Map.of(
                CHATROOM_CHANNEL_NAME, chatRoomTopic(),
                DM_CHANNEL_NAME, dmTopic()
        );
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
    public Map<String, MessageListenerAdapter> listenerAdapters(final RedisSubscriber redisSubscriber) {
        return Map.of(
                CHATROOM_LISTENER_METHOD, chatRoomListenerAdapter(redisSubscriber),
                DM_LISTENER_METHOD, dmListenerAdapter(redisSubscriber)
        );
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            final RedisConnectionFactory connectionFactory,
            final Map<String, MessageListenerAdapter> listenerAdapterMap,
            final Map<String, ChannelTopic> channelTopicMap
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterMap.get(CHATROOM_CHANNEL_NAME), channelTopicMap.get(CHATROOM_LISTENER_METHOD));
        container.addMessageListener(listenerAdapterMap.get(DM_CHANNEL_NAME), channelTopicMap.get(DM_LISTENER_METHOD));
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
