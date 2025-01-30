package com.coastee.server.user.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.dto.UserDetailElement;
import com.coastee.server.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserFacade userFacade;

    @GetMapping("/{id}")
    public ApiResponse<UserDetailElement> getProfile(
            @PathVariable("id") final Long userId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(userFacade.getProfile(userId, pageable));
    }
}
