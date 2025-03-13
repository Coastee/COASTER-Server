package com.coastee.server.user.dto.response;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.global.domain.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ScheduleElements {
    private PageInfo pageInfo;
    private List<ScheduleElement> scheduleList;

    public ScheduleElements(final Page<ChatRoom> chatRoomPage) {
        this.pageInfo = new PageInfo(chatRoomPage);
        this.scheduleList = chatRoomPage.getContent().stream()
                .map(ScheduleElement::new).toList();
    }
}
