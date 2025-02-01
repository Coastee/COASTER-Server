package com.coastee.server.chatroom.dto.request;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class CreateMeetingRequest extends CreateChatRoomRequest {
    private int maxCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String details;

    @Override
    public ChatRoom toEntity(final Server server, final User user) {
        return ChatRoom.meetingChatRoom(
                server,
                user,
                getTitle(),
                getContent(),
                startDate,
                endDate,
                location,
                details,
                maxCount
        );
    }
}
