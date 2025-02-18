package com.coastee.server.chat.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.ChatType;
import com.coastee.server.chat.dto.ChatElement;
import com.coastee.server.chat.dto.request.ChatRequest;
import com.coastee.server.chat.service.ChatService;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.service.ChatRoomEntryService;
import com.coastee.server.chatroom.service.ChatRoomService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.coastee.server.global.domain.Constant.CHATROOM_TOPIC_KEY;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFacade {
    private final Map<String, ChannelTopic> channelTopicMap;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomEntryService chatRoomEntryService;

    @Transactional
    public void chat(
            final Accessor accessor,
            final Long roomId,
            final ChatRequest chatRequest
    ) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(roomId);
        chatRoomEntryService.validateJoin(user, chatRoom);

        Chat chat = chatService.save(chatRequest.toEntity(user, chatRoom, ChatType.TALK));
        redisTemplate.convertAndSend(
                channelTopicMap.get(CHATROOM_TOPIC_KEY).getTopic(),
                new ChatElement(chat)
        );
    }

    @Transactional
    public void deleteChat(
            final Accessor accessor,
            final Long roomId,
            final Long chatId
    ) {
        User user = userService.findById(accessor.getUserId());
        ChatRoom chatRoom = chatRoomService.findById(roomId);
        chatRoomEntryService.validateJoin(user, chatRoom);

        Chat chat = chatService.findById(chatId);
        chat.validateUser(user);
        chat.delete();

        redisTemplate.convertAndSend(
                channelTopicMap.get(CHATROOM_TOPIC_KEY).getTopic(),
                new ChatElement(chat)
        );
    }
}
