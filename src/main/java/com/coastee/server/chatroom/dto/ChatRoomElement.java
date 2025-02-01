package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoomElement {
    private Long id;
    private String thumbnail;
    private String title;
    private String content;
    private PeriodElement period;

    public ChatRoomElement(final ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.thumbnail = chatRoom.getThumbnail();
        this.title = chatRoom.getTitle();
        this.content = chatRoom.getContent();
        if (chatRoom.getPeriod() != null)
            this.period = new PeriodElement(chatRoom.getPeriod());
    }
}
