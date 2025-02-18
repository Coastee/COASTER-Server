package com.coastee.server.fixture;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;

public class DMRoomFixture {

    public static DirectMessageRoom get(final User user) {
        return new DirectMessageRoom(user);
    }
}
