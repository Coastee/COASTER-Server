package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.hashtag.dto.HashTagElements;
import com.coastee.server.user.dto.UserElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoomElement {
    private Long id;
    private String thumbnail;
    private String title;
    private String content;
    private UserElement user;
    private LocalDateTime startDate;
    private int maxCount;
    private int currentCount;
    private HashTagElements hashTagElements;

    public ChatRoomElement(final ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.thumbnail = chatRoom.getThumbnail();
        this.title = chatRoom.getTitle();
        this.content = chatRoom.getContent();
        this.user = new UserElement(chatRoom.getUser());
        this.startDate = chatRoom.getStartDate();
        this.maxCount = chatRoom.getMaxCount();
        this.currentCount = chatRoom.getCurrentCount();
        this.hashTagElements = new HashTagElements(chatRoom.getTagList());
    }
}
