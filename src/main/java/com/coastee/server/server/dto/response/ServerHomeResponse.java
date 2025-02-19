package com.coastee.server.server.dto.response;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.dto.HashTagElement;
import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.dto.NoticeElements;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ServerHomeResponse {
    private List<HashTagElement> hashTagList;
    private ChatRoomElements groupChatRoom;
    private ChatRoomElements meetingChatRoom;
    private NoticeElements notice;
    private ChatElements chat;

    public ServerHomeResponse(
            final List<HashTag> hashTagList,
            final Page<ChatRoom> groupChatRoomPage,
            final Page<ChatRoom> meetingChatRoomPage
    ) {
        this.hashTagList = hashTagList.stream().map(HashTagElement::new).toList();
        this.groupChatRoom = ChatRoomElements.detail(groupChatRoomPage);
        this.meetingChatRoom = ChatRoomElements.detail(meetingChatRoomPage);
    }

    public ServerHomeResponse(
            final List<HashTag> hashTagList,
            final Page<ChatRoom> groupChatRoomPage,
            final Page<ChatRoom> meetingChatRoomPage,
            final Page<Notice> noticePage,
            final Page<Chat> chatPage
    ) {
        this.hashTagList = hashTagList.stream().map(HashTagElement::new).toList();
        this.groupChatRoom = ChatRoomElements.detail(groupChatRoomPage);
        this.meetingChatRoom = ChatRoomElements.detail(meetingChatRoomPage);
        this.notice = new NoticeElements(noticePage);
        this.chat = new ChatElements(chatPage);
    }
}
