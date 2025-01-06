package com.coastee.server.chat.dto;

import com.coastee.server.chat.domain.ChatMessageType;
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
    private Long chatId;
    private Long roomId;
    private UserElement user;
    private String content;
    private LocalDateTime createdDate;
    private ChatMessageType type;
}
