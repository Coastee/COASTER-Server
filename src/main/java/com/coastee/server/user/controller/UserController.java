package com.coastee.server.user.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/users")
public class UserController {
    private UserService userService;

    @GetMapping("/{id}")
    public ApiResponse<?> getProfile(
            @PathVariable("id") final Long userId
    ) {
        return ApiResponse.onSuccess(userService.getProfile(userId));
    }
}
