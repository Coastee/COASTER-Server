package com.coastee.server.chat.dto;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.ChatType;
import com.coastee.server.user.dto.UserElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatElement {
    private Long id;
    private UserElement user;
    private String content;
    private LocalDateTime createdDate;
    private ChatType type;
    private Long chatRoomId;

    public ChatElement(final Chat chat) {
        this.id = chat.getId();
        this.user = new UserElement(chat.getUser());
        this.content = chat.getContent();
        this.createdDate = chat.getCreatedDate();
        this.type = chat.getType();
        this.chatRoomId = chat.getChatRoom().getId();
    }
}
