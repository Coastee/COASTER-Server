package com.coastee.server.chatroom.dto.request;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.FAIL_CREATE_CHATROOM;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class CreateMeetingRequest extends CreateChatRoomRequest {
    @Min(value = 2, message = "최소 참여 인원은 2명입니다.")
    private int maxCount;

    @NotNull(message = "시작시간은 필수로 입력해야합니다.")
    private LocalDateTime startDate;

    @NotNull(message = "시작시간은 필수로 입력해야합니다.")
    private LocalDateTime endDate;

    @NotNull(message = "진행 장소는 필수로 입력해야합니다.")
    private String location;
    private String details;

    public CreateMeetingRequest(
            final String title,
            final String content,
            final Set<String> hashTags,
            final int maxCount,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final String location,
            final String details
    ) {
        super(title, content, hashTags);
        this.maxCount = maxCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.details = details;
    }

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
