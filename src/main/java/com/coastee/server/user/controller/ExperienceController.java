package com.coastee.server.user.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.dto.request.ExperienceCreateRequest;
import com.coastee.server.user.dto.request.ExperienceUpdateRequest;
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

    @PostMapping("/{experienceId}")
    @UserOnly
    public ApiResponse<Void> update(
            @Auth final Accessor accessor,
            @PathVariable("userId") final Long userId,
            @PathVariable("experienceId") final Long experienceId,
            @RequestBody final ExperienceUpdateRequest request
    ) {
        userFacade.validateAccess(accessor, userId);
        experienceFacade.update(userId, experienceId, request);
        return ApiResponse.onSuccess();
    }

    @DeleteMapping("/{experienceId}")
    @UserOnly
    public ApiResponse<Void> delete(
            @Auth final Accessor accessor,
            @PathVariable("userId") final Long userId,
            @PathVariable("experienceId") final Long experienceId
    ) {
        userFacade.validateAccess(accessor, userId);
        experienceFacade.delete(userId, experienceId);
        return ApiResponse.onSuccess();
    }
}
