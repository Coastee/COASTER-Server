package com.coastee.server.user.dto.request;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.global.dto.UpdateRequest;
import com.coastee.server.user.domain.Experience;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceUpdateRequest extends UpdateRequest {
    @Size(min = 1, max = 30, message = "경력 제목은 최소 1자 이상, 최대 30자 이하까지 설정할 수 있습니다.")
    private String title;

    @Size(max = 5, message = "세부사항은 최대 5개까지 설정할 수 있습니다.")
    private List<@Size(max = 30, message = "세부사항은 최대 30자까지 설정할 수 있습니다.") String> contentList;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public void validateNullValue(final Experience experience) {
        this.title = getOrDefault(this.title, experience.getTitle());
        this.contentList = getOrDefault(this.contentList, experience.getContentList());
        Period period = experience.getPeriod();
        this.startDate = getOrDefault(this.startDate, period.getStartDate());
        this.endDate = getOrDefault(this.endDate, period.getEndDate());
    }
}
