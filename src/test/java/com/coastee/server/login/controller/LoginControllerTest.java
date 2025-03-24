package com.coastee.server.login.controller;

import com.coastee.server.login.domain.AuthTokens;
import com.coastee.server.login.dto.request.SignupRequest;
import com.coastee.server.login.facade.LoginFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("로그인 컨트롤러 테스트")
class LoginControllerTest extends ControllerTest {
    @MockitoBean
    private LoginFacade loginFacade;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("소셜 로그인을 한다.")
    @Test
    void login() throws Exception {
        // given
        when(loginFacade.login(any()))
                .thenReturn(
                        AuthTokens.of()
                                .accessToken(ACCESS_TOKEN)
                                .refreshToken(REFRESH_TOKEN)
                                .subject(currentUser.getId().toString())
                                .build()
                );

        // when & then
        RestAssured.given(spec).log().all()
                .param("code", "12345")
                .param("redirect_uri", "https://coasterchat.com/redirect-google")
                .contentType(ContentType.JSON)
                .filter(
                        document("login",
                                queryParameters(
                                        parameterWithName("code")
                                                .description("callback 결과값 코드"),
                                        parameterWithName("redirect_uri")
                                                .description("redirect uri 값 : 초기 로그인 창에서 작성한 redirect_uri와 동일하게 전송 필요 (구글만 전달)")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.tokenType").type(STRING).description("토큰 타입: Bearer로 고정"),
                                        fieldWithPath("result.userId").type(NUMBER).description("유저 아이디"),
                                        fieldWithPath("result.accessToken").type(STRING).description("액세스 토큰"),
                                        fieldWithPath("result.refreshToken").type(STRING).description("리프레시 토큰"),
                                        fieldWithPath("result.newUser").type(BOOLEAN).description("새로운 유저인지에 대한 여부")
                                )
                        ))
                .when().get("/api/v1/login/naver-callback")
                .then().log().all().statusCode(200);
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void refreshToken() throws Exception {
        // given
        when(loginFacade.renewalToken(any(), any()))
                .thenReturn(ACCESS_TOKEN);

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .header(REFRESH_TOKEN_HEADER, REFRESH_TOKEN)
                .contentType(ContentType.JSON)
                .filter(
                        document("refresh-token",
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("만료된 액세스 토큰"),
                                        headerWithName(REFRESH_TOKEN_HEADER).description("만료되지 않은 리프레시 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.accessToken").type(STRING).description("재발급된 코드")
                                )
                        ))
                .when().post("/api/v1/refresh")
                .then().log().all();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void update() throws Exception {
        // given
        doNothing().when(loginFacade).signup(any(), any());
        SignupRequest requestDTO = new SignupRequest(
                "newnick",
                List.of("https://newUrl1", "https://newUrl2"),
                "newheadline",
                "newJob",
                5,
                "newbio",
                List.of(1L, 2L, 3L)
        );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .body(requestDTO)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("signup",
                                requestFields(
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("urlList").type(ARRAY).description("url 리스트"),
                                        fieldWithPath("headline").type(STRING).description("헤드라인"),
                                        fieldWithPath("job").type(STRING).description("직업"),
                                        fieldWithPath("expYears").type(NUMBER).description("년차"),
                                        fieldWithPath("bio").type(STRING).description("한줄소개"),
                                        fieldWithPath("serverIdList").type(ARRAY).description("초기에 설정한 서버 아이디 리스트")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/signup")
                .then().log().all().statusCode(200);
    }
}