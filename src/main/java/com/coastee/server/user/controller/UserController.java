package com.coastee.server.user.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.dto.response.UserDetailElement;
import com.coastee.server.user.dto.request.UserUpdateRequest;
import com.coastee.server.user.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserFacade userFacade;

    @GetMapping("/{id}")
    public ApiResponse<UserDetailElement> getProfile(
            @PathVariable("id") final Long userId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(userFacade.getProfile(userId, pageable));
    }

    @PostMapping("/{id}")
    @UserOnly
    public ApiResponse<Void> updateProfile(
            @Auth final Accessor accessor,
            @PathVariable("id") final Long userId,
            @RequestPart @Valid final UserUpdateRequest request,
            @RequestPart final MultipartFile image
    ) {
        userFacade.validateAccess(accessor, userId);
        userFacade.update(userId, request, image);
        return ApiResponse.onSuccess();
    }
}
