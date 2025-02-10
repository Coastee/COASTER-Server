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
import java.util.List;

import static com.coastee.server.global.domain.Constant.CURRENT_DATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceCreateRequest {
    @Size(min = 1, max = 30, message = "경력 제목은 최소 1자 이상, 최대 30자 이하까지 설정할 수 있습니다.")
    @NotNull(message = "제목은 필수로 입력해야 합니다.")
    private String title;

    @Size(max = 5, message = "세부사항은 최대 5개까지 설정할 수 있습니다.")
    private List<@Size(max = 30, message = "세부사항은 최대 30자까지 설정할 수 있습니다.") String> contentList;

    @NotNull(message = "시작 날짜는 필수로 입력해야 합니다.")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public Experience toEntity(final User user) {
        return new Experience(
                user,
                title,
                contentList,
                new Period(startDate, getEndDate())
        );
    }

    public LocalDateTime getEndDate() {
        if (this.endDate != null) return this.endDate;
        return CURRENT_DATE;
    }
}
