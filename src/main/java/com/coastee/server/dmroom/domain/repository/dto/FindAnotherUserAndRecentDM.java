package com.coastee.server.dmroom.domain.repository.dto;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAnotherUserAndRecentDM {
    private Long directMessageRoomId;
    private User user;
    private DirectMessage dm;
}
