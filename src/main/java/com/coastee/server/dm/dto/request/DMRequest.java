package com.coastee.server.dm.dto.request;

import com.coastee.server.dm.domain.DMType;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRequest {
    private Long userId;
    private Long roomId;

    @NotNull(message = "dm 내용은 필수로 전달해야 합니다.")
    private String content;

    @NotNull(message = "dm 타입은 필수로 전달해야 합니다.")
    private DMType type;

    public DirectMessage toEntity(
            final User user,
            final DirectMessageRoom directMessageRoom
    ) {
        return new DirectMessage(user, directMessageRoom, content, type);
    }

    @AssertTrue(message = "DM 채팅방 아이디 혹은 수신자 아이디를 필수로 전달해야 합니다.")
    public boolean validateRoomIdOrUserId() {
        return roomId != null || userId != null;
    }
}
