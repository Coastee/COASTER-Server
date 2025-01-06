package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.global.redis.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, Long, ChatRoom> opsHashChatRoom;
    private Map<Long, ChannelTopic> topics;

    private static final String CHAT_ROOMS = "CHAT_ROOM";

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAll() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findById(final Long id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    public ChatRoom create(final String title) {
        long id = (long) (Math.random() * 100);
        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .title(title)
                .chatRoomType(ChatRoomType.ENTIRE)
                .build();
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(final Long roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId.toString());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(final Long roomId) {
        return topics.get(roomId);
    }
}
