package com.coastee.server.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage {
    private Long id;
    private String sender;
    private ChatMessageType type;
    private Long roomId;
    @Setter
    private String message;

    @Builder
    public ChatMessage(
            final Long id,
            final Long roomId,
            final String sender,
            final ChatMessageType chatMessageType
    ) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.type = chatMessageType;
    }
}
