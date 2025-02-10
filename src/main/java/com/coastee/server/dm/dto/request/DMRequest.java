package com.coastee.server.dm.dto.request;

import com.coastee.server.dm.domain.DMType;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRequest {
    private Long roomId;
    private String content;
    private DMType type;

    public DirectMessage toEntity(
            final User user,
            final DirectMessageRoom directMessageRoom
    ) {
        return new DirectMessage(user, directMessageRoom, content, type);
    }
}
