package com.coastee.server.user.facade;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.UserDetailElement;
import com.coastee.server.user.service.ExperienceService;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final ExperienceService experienceService;

    public UserDetailElement getProfile(final Long userId, final Pageable pageable) {
        User user = userService.findById(userId);
        Page<Experience> experiencePage = experienceService.findAllByUser(user, pageable);
        return UserDetailElement.from()
                .user(user)
                .experiencePage(experiencePage)
                .build();
    }
}
