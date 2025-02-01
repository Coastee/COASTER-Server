package com.coastee.server.chatroom.dto.request;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_CREATE_CHATROOM;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class CreateMeetingRequest extends CreateChatRoomRequest {
    @Size(min = 2, message = "최소 참여 인원은 2명입니다.")
    private int maxCount;

    @NotNull(message = "시작시간은 필수로 입력해야합니다.")
    private LocalDateTime startDate;

    @NotNull(message = "시작시간은 필수로 입력해야합니다.")
    private LocalDateTime endDate;

    @NotNull(message = "진행 장소는 필수로 입력해야합니다.")
    private String location;
    private String details;

    @Override
    public ChatRoom toEntity(
            final Server server,
            final User user,
            final ChatRoomType type
    ) {
        if (type.equals(ChatRoomType.GROUP)) throw new GeneralException(FAIL_CREATE_CHATROOM);
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
