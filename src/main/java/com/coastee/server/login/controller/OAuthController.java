package com.coastee.server.login.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.login.dto.response.AccessTokenResponse;
import com.coastee.server.login.dto.response.OAuthUserResponse;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.login.service.LoginService;
import com.coastee.server.login.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.coastee.server.global.Constant.HEADER_AUTHORIZATION;
import static com.coastee.server.global.Constant.HEADER_REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    private final LoginService loginService;

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

    @PostMapping("/api/v1/refresh")
    public ApiResponse<AccessTokenResponse> refreshToken(
            @RequestHeader(HEADER_AUTHORIZATION) final String accessToken,
            @RequestHeader(HEADER_REFRESH_TOKEN) final String refreshToken
    ) {
        String renewalToken = loginService.refreshToken(accessToken, refreshToken);
        return ApiResponse.onSuccess(new AccessTokenResponse(renewalToken));
    }
}
