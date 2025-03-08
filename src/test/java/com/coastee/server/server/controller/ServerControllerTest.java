package com.coastee.server.server.controller;

import com.coastee.server.fixture.ServerFixture;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.server.dto.ServerElements;
import com.coastee.server.server.facade.ServerFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("서버 컨트롤러 테스트")
class ServerControllerTest extends ControllerTest {

    @MockitoBean
    private ServerFacade serverFacade;

    @Autowired
    private ServerRepository serverRepository;

    @DisplayName("모든 서버를 조회한다.")
    @Test
    void findAll() throws Exception {
        // given
        List<Server> servers = serverRepository.saveAll(ServerFixture.getAll());
        when(serverFacade.findWithConditions(any())).thenReturn(new ServerElements(servers));

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("scope", "joined")
                .contentType(ContentType.JSON)
                .filter(
                        document("find-servers",
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰 - scope : `joined` 시에만 전송해도 됨.")
                                ),
                                queryParameters(
                                        parameterWithName("scope")
                                                .description("""
                                                        조회 기준 - `joined` : 참여한 서버 조회(단, joined 사용 시 header로 token 전송 필수),
                                                        `all` : 전체 서버 조회(header token 전송 필수 X) (default: 전체 조회)
                                                        """)
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.count").type(NUMBER).description("서버 개수"),
                                        fieldWithPath("result.serverList").type(ARRAY).description("서버 리스트"),
                                        fieldWithPath("result.serverList[].id").type(NUMBER).description("서버 리스트 ; 서버 아이디"),
                                        fieldWithPath("result.serverList[].title").type(STRING).description("서버 리스트 ; 서버 제목")
                                )
                        ))
                .when().get("/api/v1/servers")
                .then().log().all().statusCode(200);
    }

    @DisplayName("서버에서 탈퇴한다.")
    @Test
    void exit() throws Exception {
        // given
        doNothing().when(serverFacade).enter(any(), any());

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
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

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .filter(
                        document("enter-server",
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/servers/{serverId}", 1)
                .then().log().all().statusCode(200);
    }
}