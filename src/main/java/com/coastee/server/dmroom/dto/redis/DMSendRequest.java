package com.coastee.server.dmroom.dto.redis;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.dto.DMElement;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.dto.DMRoomElement;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMSendRequest {
    private Long senderId;
    private Long receiverId;
    private DMElement dm;
    private DMRoomElement dmRoom;

    public DMSendRequest(
            final User sender,
            final User receiver,
            final DirectMessage dm,
            final DirectMessageRoom dmRoom
    ) {
        this.senderId = sender.getId();
        this.receiverId = receiver.getId();
        this.dm = new DMElement(dm);
        this.dmRoom = new DMRoomElement(dmRoom, sender, dm);
    }
}
