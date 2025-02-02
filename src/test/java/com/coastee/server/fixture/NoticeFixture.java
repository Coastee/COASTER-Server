package com.coastee.server.fixture;

import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;

import java.util.List;

public class NoticeFixture {

    public static Notice get(final Server server, final User user) {
        return new Notice(server, user, "title", "content");
    }

    public static List<Notice> getAll(final Server server, final User user) {
        return List.of(
                new Notice(server, user, "titleA", "contentA"),
                new Notice(server, user, "titleB", "contentB"),
                new Notice(server, user, "titleC", "contentC")
        );
    }
}
