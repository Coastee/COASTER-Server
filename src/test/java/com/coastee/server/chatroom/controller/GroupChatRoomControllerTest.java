package com.coastee.server.chatroom.controller;

import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.global.ControllerTest;
import com.coastee.server.server.domain.repository.ServerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("그룹챗 테스트")
class GroupChatRoomControllerTest extends ControllerTest {

    @MockitoBean
    private ChatRoomFacade chatRoomFacade;

    @Autowired
    private ServerRepository serverRepository;

    @DisplayName("그룹챗을 개설한다.")
    @Test
    void findAll() throws Exception {
        // given
        doNothing().when(chatRoomFacade).create(any(), any(), any());
        CreateGroupChatRequest body = new CreateGroupChatRequest("title", "content");

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .body(body)
                .filter(
                        document("create-groupchat",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰 - 그룹챗 개설자")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("그룹챗 제목"),
                                        fieldWithPath("content").type(STRING).description("그룹챗 설명")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/servers/{serverId}/groups", 1)
                .then().log().all().statusCode(200);
    }

}