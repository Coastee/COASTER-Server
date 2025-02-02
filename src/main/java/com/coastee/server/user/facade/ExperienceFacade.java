package com.coastee.server.user.facade;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.request.ExperienceCreateRequest;
import com.coastee.server.user.service.ExperienceService;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceFacade {
    private final UserService userService;
    private final ExperienceService experienceService;

    public void create(final Long userId, final ExperienceCreateRequest request) {
        User user = userService.findById(userId);
        experienceService.save(request.toEntity(user));
    }

    public void delete(final Long userId, final Long experienceId) {
        User user = userService.findById(userId);
        Experience experience = experienceService.findById(experienceId);
        experience.validateUser(user);
        experience.delete();
    }
}
