package com.coastee.server.user.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.dto.request.ExperienceCreateRequest;
import com.coastee.server.user.facade.ExperienceFacade;
import com.coastee.server.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/{userId}/experiences")
public class ExperienceController {
    private final ExperienceFacade experienceFacade;
    private final UserFacade userFacade;

    @PostMapping("")
    @UserOnly
    public ApiResponse<Void> create(
            @Auth final Accessor accessor,
            @PathVariable("userId") final Long userId,
            @RequestBody final ExperienceCreateRequest request
    ) {
        userFacade.validateAccess(accessor, userId);
        experienceFacade.create(userId, request);
        return ApiResponse.onSuccess();
    }
}
