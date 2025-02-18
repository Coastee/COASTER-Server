package com.coastee.server.fixture;

import com.coastee.server.dm.domain.DMType;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;

public class DMFixture {

    public static DirectMessage get(final User user, final DirectMessageRoom dmRoom) {
        return new DirectMessage(user, dmRoom, "안녕하세요!", DMType.TALK);
    }
}
