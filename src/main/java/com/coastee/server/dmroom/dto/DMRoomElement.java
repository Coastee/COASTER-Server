package com.coastee.server.dmroom.dto;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.dto.DMElement;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.UserElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRoomElement {
    private Long id;
    private UserElement user;
    private DMElement dm;

    public DMRoomElement(
            final DirectMessageRoom room,
            final User user,
            final DirectMessage dm
    ) {
        this.id = room.getId();
        this.user = new UserElement(user);
        this.dm = new DMElement(dm);
    }
}
