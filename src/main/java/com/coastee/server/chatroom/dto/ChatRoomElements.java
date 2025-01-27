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
    private List<ChatRoomElement> chatRoomList;

    public ChatRoomElements(Page<ChatRoom> chatRoomPage) {
        this.pageInfo = new PageInfo(chatRoomPage);
        this.chatRoomList = chatRoomPage.getContent().stream().map(ChatRoomElement::new).toList();
    }
}
