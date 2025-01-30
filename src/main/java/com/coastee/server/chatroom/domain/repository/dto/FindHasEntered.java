package com.coastee.server.chatroom.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindHasEntered {
    private Long chatRoomId;
    private Boolean hasJoined;
}
