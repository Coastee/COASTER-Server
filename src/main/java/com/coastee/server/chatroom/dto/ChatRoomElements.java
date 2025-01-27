package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.global.domain.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoomElements {
    private PageInfo pageInfo;
    private List<? extends ChatRoomElement> chatRoomList;

    public ChatRoomElements(Page<ChatRoom> chatRoomPage) {
        this.pageInfo = new PageInfo(chatRoomPage);
        this.chatRoomList = chatRoomPage.getContent().stream().map(ChatRoomElement::new).toList();
    }

    public static ChatRoomElements from(Page<ChatRoom> chatRoomPage) {
        return new ChatRoomElements(
                new PageInfo(chatRoomPage),
                chatRoomPage.getContent().stream().map(ChatRoomElement::new).toList()
        );
    }

    public static ChatRoomElements detail(Page<ChatRoom> chatRoomPage) {
        return new ChatRoomElements(
                new PageInfo(chatRoomPage),
                chatRoomPage.getContent().stream().map(ChatRoomDetailElement::new).toList()
        );
    }
}
