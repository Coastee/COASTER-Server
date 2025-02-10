package com.coastee.server.user.dto;

import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class UserElements {
    private PageInfo pageInfo;
    private List<UserElement> userList;

    public UserElements(final Page<User> userPage) {
        this.pageInfo = new PageInfo(userPage);
        this.userList = userPage.getContent().stream().map(UserElement::new).toList();
    }

    public static UserElements from(final Page<ChatRoomEntry> entryPage) {
        return new UserElements(
                new PageInfo(entryPage),
                entryPage.getContent().stream()
                        .map(e -> new UserElement(e.getUser()))
                        .toList()
        );
    }
}
