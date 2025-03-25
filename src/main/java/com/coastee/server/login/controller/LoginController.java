package com.coastee.server.login.controller;

import com.coastee.server.auth.Auth;
import com.coastee.server.auth.UserOnly;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.ApiResponse;
import com.coastee.server.global.util.CookieUtil;
import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.dto.request.SignupRequest;
import com.coastee.server.login.dto.response.AccessTokenResponse;
import com.coastee.server.login.facade.LoginFacade;
import com.coastee.server.login.infrastructure.JwtHeaderUtil;
import com.coastee.server.login.infrastructure.RedirectUriUtil;
import com.coastee.server.login.infrastructure.SessionManager;
import com.coastee.server.login.infrastructure.loginparams.GoogleLoginParams;
import com.coastee.server.login.infrastructure.loginparams.KakaoLoginParams;
import com.coastee.server.login.infrastructure.loginparams.LinkedInLoginParams;
import com.coastee.server.login.infrastructure.loginparams.NaverLoginParams;
import com.coastee.server.server.facade.ServerFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.coastee.server.global.domain.Constant.*;

@RestController
@Validated
@RequiredArgsConstructor
public class LoginController {
    private final LoginFacade loginFacade;
    private final ServerFacade serverFacade;
    private final SessionManager sessionManager;
    private final RedirectUriUtil redirectUriUtil;
    private final CookieUtil cookieUtil;

    @GetMapping("/api/v1/login/naver-callback")
    public ApiResponse<AuthTokens> naverLogin(
            @ModelAttribute final NaverLoginParams naverLoginParams
    ) throws IOException {
        return ApiResponse.onSuccess(loginFacade.login(naverLoginParams));
    }

    @GetMapping("/api/v1/login/kakao-callback")
    public ApiResponse<AuthTokens> kakaoLogin(
            @ModelAttribute final KakaoLoginParams kakaoLoginParams
    ) throws IOException {
        return ApiResponse.onSuccess(loginFacade.login(kakaoLoginParams));
    }

    @GetMapping("/api/v1/login/google-callback")
    public ApiResponse<AuthTokens> googleLogin(
            @ModelAttribute final GoogleLoginParams googleLoginParams
    ) throws IOException {
        return ApiResponse.onSuccess(loginFacade.login(googleLoginParams));
    }

    @PostMapping("/api/v1/signup")
    public ApiResponse<Void> signup(
            @Auth final Accessor accessor,
            @RequestBody final SignupRequest signupRequest
    ) {
        loginFacade.signup(accessor, signupRequest);
        return ApiResponse.onSuccess();
    }

    @GetMapping("/api/v1/connect/linkedin")
    @UserOnly
    public void connectLinkedin(
            @Auth final Accessor accessor,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        sessionManager.setSession(accessor.getUserId(), request);
        response.sendRedirect(redirectUriUtil.getLinkedinRedirectUri());
    }

    @GetMapping("/api/v1/login/linkedin-callback")
    public ApiResponse<Void> connectLinkedIn(
            @ModelAttribute final LinkedInLoginParams linkedInLoginParams,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {
        Long userId = sessionManager.getUserId(request);
        loginFacade.connect(Accessor.user(userId), linkedInLoginParams);
        sessionManager.removeSession(request);
        response.sendRedirect(redirectUriUtil.getProfileUri(userId));
        return ApiResponse.onSuccess();
    }

    @PostMapping("/api/v1/refresh")
    public ApiResponse<AccessTokenResponse> renewalToken(
            @RequestHeader(HEADER_AUTHORIZATION)
            @Size(min = TOKEN_PREFIX_LENGTH, message = "접근 토큰은 필수적으로 전달되어야합니다.") final String accessToken,
            @RequestHeader(HEADER_REFRESH_TOKEN)
            @Size(min = TOKEN_PREFIX_LENGTH, message = "접근 토큰은 필수적으로 전달되어야합니다.") final String refreshToken
    ) {
        String renewalToken = loginFacade.renewalToken(
                JwtHeaderUtil.extractToken(accessToken),
                JwtHeaderUtil.extractToken(refreshToken)
        );
        return ApiResponse.onSuccess(new AccessTokenResponse(renewalToken));
    }
}
