package com.coastee.server.dm.dto.request;

import com.coastee.server.dm.domain.DMType;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.BAD_DM_REQUEST;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRequest {
    private Long userId;

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

    public void validateUserId() {
        if (this.userId == null) {
            throw new GeneralException(BAD_DM_REQUEST);
        }
    }
}
