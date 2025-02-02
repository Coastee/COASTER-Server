package com.coastee.server.fixture;

import com.coastee.server.chatroom.domain.Period;
import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class ExperienceFixture {

    public static Experience get(final User user) {
        return new Experience(
                user,
                "숙명 기업 인턴",
                "인턴십 과정을을 진행하여 000 결과에 기여하였습니다.",
                new Period(LocalDateTime.now().minusYears(1L), LocalDateTime.now())
        );
    }

    public static List<Experience> getAll(final User user) {
        return List.of(
                new Experience(
                        user,
                        "B 기업 개발팀장",
                        "000 결과에 기여하였습니다.",
                        new Period(LocalDateTime.now().minusYears(1L), LocalDateTime.now())
                ),
                new Experience(
                        user,
                        "A 기업 개발팀",
                        "000 결과에 기여하였습니다.",
                        new Period(LocalDateTime.now().minusYears(1L), LocalDateTime.now())
                ),
                new Experience(
                        user,
                        "숙명 기업 인턴",
                        "인턴십 과정을 진행하여 000 결과에 기여하였습니다.",
                        new Period(LocalDateTime.now().minusYears(1L), LocalDateTime.now())
                )
        );
    }
}
