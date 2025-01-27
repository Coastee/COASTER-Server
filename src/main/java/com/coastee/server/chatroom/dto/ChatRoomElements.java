package com.coastee.server.chatroom.dto;

import com.coastee.server.global.domain.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatRoomElements {
    private PageInfo pageInfo;
    private List<ChatRoomElements> chatRoomList;
}
