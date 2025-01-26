package com.coastee.server.user.service;

import com.coastee.server.fixture.UserFixture;
import com.coastee.server.global.ServiceTest;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import com.coastee.server.user.dto.response.ProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest extends ServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserFixture.get());
    }

    @DisplayName("[프로필 조회] 회원의 프로필이 정상적으로 조회된다.")
    @Test
    void getProfile() throws Exception {
        // when
        ProfileResponse actual = userService.getProfile(user.getId());

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(ProfileResponse.from(user));
    }

    @DisplayName("[프로필 조회] 유효하지 않은 아이디일시, 에러가 발생한다.")
    @Test
    void getProfile_withInvalidId() throws Exception {
        // when & then
        GeneralException e = assertThrows(GeneralException.class, () -> userService.getProfile(0L));
        assertEquals(INVALID_USER_ID, e.getCode());
    }
}