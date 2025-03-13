package com.coastee.server.user.dto.response;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.dto.AddressElement;
import com.coastee.server.chatroom.dto.PeriodElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ScheduleElement {
    private Long id;
    private String title;
    private PeriodElement period;
    private AddressElement address;

    public ScheduleElement(final ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.period = new PeriodElement(chatRoom.getPeriod());
        this.address = new AddressElement(chatRoom.getAddress());
    }
}
