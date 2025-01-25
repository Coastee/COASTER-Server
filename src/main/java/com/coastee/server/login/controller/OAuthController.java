package com.coastee.server.login.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.login.dto.response.OAuthUserResponse;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.login.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/api/v1/naver/login")
    public ApiResponse<OAuthUserResponse> naverLogin(@ModelAttribute NaverLoginParams naverLoginParams) {
        return ApiResponse.onSuccess(oAuthService.login(naverLoginParams));
    }

    @GetMapping("/api/v1/kakao/login")
    public ApiResponse<OAuthUserResponse> kakaoLogin(@ModelAttribute KakaoLoginParams kakaoLoginParams) {
        return ApiResponse.onSuccess(oAuthService.login(kakaoLoginParams));
    }
}
