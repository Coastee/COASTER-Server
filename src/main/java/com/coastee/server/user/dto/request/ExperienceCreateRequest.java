package com.coastee.server.user.dto.request;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.coastee.server.global.domain.Constant.CURRENT_DATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceCreateRequest {
    @Size(min = 1)
    @NotNull(message = "제목은 필수로 입력해야 합니다.")
    private String title;

    private String content;

    @NotNull(message = "시작 날짜는 필수로 입력해야 합니다.")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public Experience toEntity(final User user) {
        return new Experience(
                user,
                title,
                content,
                new Period(startDate, getEndDate())
        );
    }

    private LocalDateTime getEndDate() {
        if (endDate != null) return endDate;
        return CURRENT_DATE;
    }
}
