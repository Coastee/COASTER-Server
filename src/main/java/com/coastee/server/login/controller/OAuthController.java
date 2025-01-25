package com.coastee.server.login.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.login.dto.response.OAuthUserResponse;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
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

    @GetMapping("/api/v1/login/naver-callback")
    public ApiResponse<OAuthUserResponse> naverLogin(@ModelAttribute final NaverLoginParams naverLoginParams) {
        return ApiResponse.onSuccess(oAuthService.login(naverLoginParams));
    }

    @GetMapping("/api/v1/login/kakao-callback")
    public ApiResponse<OAuthUserResponse> kakaoLogin(@ModelAttribute final KakaoLoginParams kakaoLoginParams) {
        return ApiResponse.onSuccess(oAuthService.login(kakaoLoginParams));
    }

    @GetMapping("/api/v1/login/google-callback")
    public ApiResponse<OAuthUserResponse> googleLogin(@ModelAttribute final GoogleLoginParams googleLoginParams) {
        return ApiResponse.onSuccess(oAuthService.login(googleLoginParams));
    }
}
