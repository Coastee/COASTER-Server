package com.coastee.server.fixture;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class ExperienceFixture {

    public static Experience get(final User user) {
        return Experience.of()
                .user(user)
                .title("숙명 기업 인턴")
                .startDate(LocalDateTime.now().minusYears(1L))
                .endDate(LocalDateTime.now())
                .content("인턴을 진행하여 000 결과에 기여하였습니다.")
                .build();
    }

    public static List<Experience> getAll(final User user) {
        return List.of(
                Experience.of()
                        .user(user)
                        .title("B 기업 개발팀장")
                        .startDate(LocalDateTime.now().minusYears(2L))
                        .endDate(LocalDateTime.now())
                        .content("000 결과에 기여하였습니다.")
                        .build(),
                Experience.of()
                        .user(user)
                        .title("A 기업 개발팀")
                        .startDate(LocalDateTime.now().minusYears(4L))
                        .endDate(LocalDateTime.now().minusYears(2L))
                        .content("000 결과에 기여하였습니다.")
                        .build(),
                Experience.of()
                        .user(user)
                        .title("숙명 기업 인턴")
                        .startDate(LocalDateTime.now().minusYears(5L))
                        .endDate(LocalDateTime.now().minusYears(4L))
                        .content("인턴을 진행하여 000 결과에 기여하였습니다.")
                        .build()
        );
    }
}
