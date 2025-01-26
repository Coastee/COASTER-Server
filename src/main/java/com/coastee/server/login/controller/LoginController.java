package com.coastee.server.login.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.dto.response.AccessTokenResponse;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.coastee.server.global.Constant.HEADER_AUTHORIZATION;
import static com.coastee.server.global.Constant.HEADER_REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/api/v1/login/naver-callback")
    public ApiResponse<AuthTokens> naverLogin(@ModelAttribute final NaverLoginParams naverLoginParams) {
        return ApiResponse.onSuccess(loginService.login(naverLoginParams));
    }

    @GetMapping("/api/v1/login/kakao-callback")
    public ApiResponse<AuthTokens> kakaoLogin(@ModelAttribute final KakaoLoginParams kakaoLoginParams) {
        return ApiResponse.onSuccess(loginService.login(kakaoLoginParams));
    }

    @GetMapping("/api/v1/login/google-callback")
    public ApiResponse<AuthTokens> googleLogin(@ModelAttribute final GoogleLoginParams googleLoginParams) {
        return ApiResponse.onSuccess(loginService.login(googleLoginParams));
    }

    @PostMapping("/api/v1/refresh")
    public ApiResponse<AccessTokenResponse> refreshToken(
            @RequestHeader(HEADER_AUTHORIZATION) final String accessToken,
            @RequestHeader(HEADER_REFRESH_TOKEN) final String refreshToken
    ) {
        String renewalToken = loginService.refreshToken(accessToken, refreshToken);
        return ApiResponse.onSuccess(new AccessTokenResponse(renewalToken));
    }
}
