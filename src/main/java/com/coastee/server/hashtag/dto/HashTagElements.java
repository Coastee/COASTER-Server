package com.coastee.server.hashtag.dto;

import com.coastee.server.chatroom.domain.ChatRoomTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class HashTagElements {
    private List<HashTagElement> hashTagList;

    public HashTagElements(final List<ChatRoomTag> chatRoomTagList) {
        this.hashTagList = chatRoomTagList.stream().map(
                chatRoomTag -> new HashTagElement(chatRoomTag.getHashTag())
        ).toList();
    }
}
