package com.coastee.server.server.controller;

import com.coastee.server.global.ControllerTest;
import com.coastee.server.server.dto.request.ServerEntryRequest;
import com.coastee.server.server.facade.ServerFacade;
import com.coastee.server.user.domain.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("서버 컨트롤러 테스트")
class ServerControllerTest extends ControllerTest {
    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private ServerFacade serverFacade;
    
    @DisplayName("서버에서 탈퇴한다.")
    @Test
    void exit() throws Exception{
        // given
        doNothing().when(serverFacade).enter(any(), any());
        ServerEntryRequest request = new ServerEntryRequest(List.of(1L, 2L, 3L));

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .filter(
                        document("exit-server",
                                pathParameters(
                                    parameterWithName("serverId").description("서버 아이디")
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
                .when().delete("/api/v1/servers/{serverId}", 1)
                .then().log().all().statusCode(200);
    }

    @DisplayName("서버에 참여하다.")
    @Test
    void enter() throws Exception {
        // given
        doNothing().when(serverFacade).enter(any(), any());
        ServerEntryRequest request = new ServerEntryRequest(List.of(1L, 2L, 3L));

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .filter(
                        document("enter-server",
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("serverIdList").type(ARRAY)
                                                .description("사용자가 참여한 서버의 아이디들을 모두 리스트 형태로 전달하면 됩니다.")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/servers/enter")
                .then().log().all().statusCode(200);
    }
}