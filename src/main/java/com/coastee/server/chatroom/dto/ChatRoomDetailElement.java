package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.hashtag.dto.HashTagElement;
import com.coastee.server.user.dto.UserElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoomDetailElement extends ChatRoomElement {
    private UserElement user;
    private boolean hasEntered;
    private int maxCount;
    private int currentCount;
    private List<HashTagElement> hashTagList;

    public ChatRoomDetailElement(final ChatRoom chatRoom, final Boolean hasEntered) {
        super(chatRoom);
        this.user = new UserElement(chatRoom.getUser());
        this.hasEntered = hasEntered;
        this.maxCount = chatRoom.getMaxCount();
        this.currentCount = chatRoom.getCurrentCount();
        this.hashTagList = chatRoom.getTagList().stream().map(
                chatRoomTag -> new HashTagElement(chatRoomTag.getHashTag())
        ).toList();
    }
}
