package com.coastee.server.login.controller;

import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.dto.response.AccessTokenResponse;
import com.coastee.server.login.infrastructure.JwtHeaderUtil;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.loginparams.LinkedInLoginParams;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.login.service.LoginService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.coastee.server.global.domain.Constant.*;

@RestController
@Validated
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

    @GetMapping("/api/v1/login/linkedin-callback")
    public ApiResponse<Void> connectLinkedin(@ModelAttribute final LinkedInLoginParams linkedInLoginParams) {
        loginService.connect(linkedInLoginParams);
        return ApiResponse.onSuccess();
    }

    @PostMapping("/api/v1/refresh")
    public ApiResponse<AccessTokenResponse> renewalToken(
            @RequestHeader(HEADER_AUTHORIZATION)
            @Size(min = TOKEN_PREFIX_LENGTH, message = "접근 토큰은 필수적으로 전달되어야합니다.") final String accessToken,
            @RequestHeader(HEADER_REFRESH_TOKEN)
            @Size(min = TOKEN_PREFIX_LENGTH, message = "접근 토큰은 필수적으로 전달되어야합니다.") final String refreshToken
    ) {
        String renewalToken = loginService.renewalToken(
                JwtHeaderUtil.extractToken(accessToken),
                JwtHeaderUtil.extractToken(refreshToken)
        );
        return ApiResponse.onSuccess(new AccessTokenResponse(renewalToken));
    }
}
