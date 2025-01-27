package com.coastee.server.chatroom.controller;

import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.facade.GroupChatRoomFacade;
import com.coastee.server.global.ControllerTest;
import com.coastee.server.server.domain.repository.ServerRepository;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.coastee.server.util.FileUtil.getFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("그룹챗 테스트")
class GroupChatRoomControllerTest extends ControllerTest {

    @MockitoBean
    private GroupChatRoomFacade groupChatRoomFacade;

    @Autowired
    private ServerRepository serverRepository;

    @DisplayName("그룹챗을 개설한다.")
    @Test
    void create() throws Exception {
        // given
        doNothing().when(groupChatRoomFacade).create(any(), any(), any(), any());
        CreateGroupChatRequest requestDTO = new CreateGroupChatRequest("title", "content");

        MultiPartSpecification request = new MultiPartSpecBuilder(requestDTO, ObjectMapperType.JACKSON_2)
                .controlName("request")
                .mimeType(MediaType.APPLICATION_JSON_VALUE)
                .charset("UTF-8")
                .build();

        MultiPartSpecification file =
                new MultiPartSpecBuilder(getFile())
                        .controlName("image")
                        .mimeType(MediaType.IMAGE_PNG_VALUE)
                        .charset("UTF-8")
                        .build();

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .multiPart(request)
                .multiPart(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .filter(
                        document("create-groupchat",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                requestParts(
                                        partWithName("image").description("(opt) 그룹챗 썸네일 이미지 파일"),
                                        partWithName("request").description("제목, 설명 등 json data")
                                ),
                                requestPartFields(
                                        "request",
                                        fieldWithPath("title").type(STRING).description("``request.title`` 그룹챗 제목 : 최소 1자 ~ 최대 20자"),
                                        fieldWithPath("content").type(STRING).description("``request.content`` 그룹챗 설명 : 최대 150자")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰 - 그룹챗 개설자")
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