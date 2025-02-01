package com.coastee.server.chatroom.util;

import com.coastee.server.chatroom.domain.ChatRoomType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomTypeConverter implements Converter<String, ChatRoomType> {

    @Override
    public ChatRoomType convert(final String source) {
        return ChatRoomType.url(source);
    }
}
