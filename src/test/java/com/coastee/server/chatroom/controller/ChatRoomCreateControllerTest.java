package com.coastee.server.chatroom.controller;

import com.coastee.server.chat.domain.reposistory.ChatRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomTagRepository;
import com.coastee.server.chatroom.dto.request.ChatRoomCreateRequest;
import com.coastee.server.chatroom.dto.request.MeetingCreateRequest;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Set;

import static com.coastee.server.util.FileUtil.getFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("채팅방 생성 컨트롤러 테스트")
public class ChatRoomCreateControllerTest extends ControllerTest {
    @MockitoBean
    private ChatRoomFacade chatRoomFacade;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private ChatRoomTagRepository chatRoomTagRepository;

    @Autowired
    private ChatRepository chatRepository;

    @DisplayName("그룹챗을 개설한다.")
    @Test
    void createGroup() throws Exception {
        // given
        doNothing().when(chatRoomFacade).create(any(), any(), any(), any(), any());
        ChatRoomCreateRequest requestDTO = new ChatRoomCreateRequest("title", "content", Set.of("#A", "#B"));

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
                        document("create-group",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                requestParts(
                                        partWithName("image").description("(opt) 썸네일 이미지 파일"),
                                        partWithName("request").description("제목, 설명 등 json data")
                                ),
                                requestPartFields(
                                        "request",
                                        fieldWithPath("title").type(STRING).description("``request.title`` 제목 : 최소 1자 ~ 최대 20자"),
                                        fieldWithPath("content").type(STRING).description("``request.content`` 설명 : 최대 80자"),
                                        fieldWithPath("hashTags").type(ARRAY).description("``request.hashTags`` 해시태그 : 최대 10개")
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
                .when().post("/api/v1/servers/{serverId}/groups", 1)
                .then().log().all().statusCode(200);
    }

    @DisplayName("커피챗을 개설한다.")
    @Test
    void createMeeting() throws Exception {
        // given
        doNothing().when(chatRoomFacade).create(any(), any(), any(), any(), any());
        ChatRoomCreateRequest requestDTO = new MeetingCreateRequest(
                "title",
                "content",
                Set.of("#A", "#B"),
                5,
                LocalDateTime.now().minusDays(1).minusHours(2),
                LocalDateTime.now().minusDays(1),
                "서울특별시 용산구 청파로47길 100",
                "숙명여자대학교 1캠퍼스 정문 앞"
        );

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
                        document("create-meeting",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                requestParts(
                                        partWithName("image").description("(opt) 썸네일 이미지 파일"),
                                        partWithName("request").description("제목, 설명 등 json data")
                                ),
                                requestPartFields(
                                        "request",
                                        fieldWithPath("title").type(STRING).description("``request.title`` 제목 : 최소 1자 ~ 최대 20자"),
                                        fieldWithPath("content").type(STRING).description("``request.content`` 설명 : 최대 80자"),
                                        fieldWithPath("hashTags").type(ARRAY).description("``request.hashTags`` 해시태그 : 최대 10개"),
                                        fieldWithPath("maxCount").type(NUMBER).description("``request.startDate`` 참여인원"),
                                        fieldWithPath("startDate").type(ARRAY).description("``request.startDate`` 시작시간"),
                                        fieldWithPath("endDate").type(ARRAY).description("``request.endDate`` 종료시간"),
                                        fieldWithPath("location").type(STRING).description("``request.location`` 장소"),
                                        fieldWithPath("details").type(STRING).description("``request.details`` 장소 설명")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰 - 커피챗 개설자")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/servers/{serverId}/meetings", 1)
                .then().log().all().statusCode(200);
    }
}
