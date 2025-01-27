package com.coastee.server.chatroom.controller;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomTag;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomTagRepository;
import com.coastee.server.chatroom.dto.ChatRoomElement;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.chatroom.facade.GroupChatRoomFacade;
import com.coastee.server.fixture.HashTagFixture;
import com.coastee.server.fixture.ServerFixture;
import com.coastee.server.fixture.UserFixture;
import com.coastee.server.global.ControllerTest;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.coastee.server.util.FileUtil.getFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("그룹챗 테스트")
class GroupChatRoomControllerTest extends ControllerTest {

    @MockitoBean
    private GroupChatRoomFacade groupChatRoomFacade;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private ChatRoomTagRepository chatRoomTagRepository;

    @DisplayName("그룹챗을 찾는다.")
    @Test
    void findAll() throws Exception {
        // given
        User userA = userRepository.save(UserFixture.get("userA"));
        User userB = userRepository.save(UserFixture.get("userB"));
        User userC = userRepository.save(UserFixture.get("userC"));
        Server server = serverRepository.save(ServerFixture.get());
        List<ChatRoom> chatRoomList =
                List.of(
                        chatRoomRepository.save(ChatRoom.groupChatRoom(server, userA, "titleA", "contentA")),
                        chatRoomRepository.save(ChatRoom.groupChatRoom(server, userB, "titleB", "contentB")),
                        chatRoomRepository.save(ChatRoom.groupChatRoom(server, userC, "titleC", "contentC"))
                );
        HashTag hashTagA = hashTagRepository.save(HashTagFixture.get("#A"));
        HashTag hashTagB = hashTagRepository.save(HashTagFixture.get("#B"));
        HashTag hashTagC = hashTagRepository.save(HashTagFixture.get("#C"));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(0), hashTagA));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(0), hashTagB));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(1), hashTagA));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(1), hashTagB));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(1), hashTagC));
        chatRoomTagRepository.save(new ChatRoomTag(chatRoomList.get(2), hashTagA));

        when(groupChatRoomFacade.findByScope(any(), any(), any(), any()))
                .thenReturn(new ChatRoomElements(
                                new PageInfo(true, 0, 3, 40),
                                chatRoomList.stream().map(ChatRoomElement::new).toList()
                        )
                );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("page", "0")
                .param("sort", "name")
                .param("scope", "all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("find-all-groupchat",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)"),
                                        parameterWithName("sort")
                                                .description("정렬기준 - `name` : 기본순, `remain` : 마감임박순 (default: 최신순)"),
                                        parameterWithName("scope")
                                                .description("조회 기준 - `joined` : 참여한 그룹챗 조회 (default: 전체 조회)")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰 - 그룹챗 개설자")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.pageInfo").type(OBJECT).description("페이징 정보"),
                                        fieldWithPath("result.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.pageInfo.totalPages").type(NUMBER).description("총 페이지 개수"),
                                        fieldWithPath("result.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.pageInfo.size").type(NUMBER).description("페이지 사이즈"),
                                        fieldWithPath("result.chatRoomList").type(ARRAY).description("채팅방 리스트"),
                                        fieldWithPath("result.chatRoomList[].id").type(NUMBER).description("채팅방 아이디"),
                                        fieldWithPath("result.chatRoomList[].thumbnail").type(STRING).description("채팅방 썸네일").optional(),
                                        fieldWithPath("result.chatRoomList[].title").type(STRING).description("채팅방 제목"),
                                        fieldWithPath("result.chatRoomList[].content").type(STRING).description("채팅방 설명"),
                                        fieldWithPath("result.chatRoomList[].user").type(OBJECT).description("채팅방 개설자"),
                                        fieldWithPath("result.chatRoomList[].user.id").type(NUMBER).description("개설자 아이디"),
                                        fieldWithPath("result.chatRoomList[].user.profileImage").type(STRING).description("개설자 프로필 사진"),
                                        fieldWithPath("result.chatRoomList[].user.nickname").type(STRING).description("개설자 닉네임"),
                                        fieldWithPath("result.chatRoomList[].user.headline").type(STRING).description("개설자 한줄소개"),
                                        fieldWithPath("result.chatRoomList[].startDate").type(STRING).description("채팅방 만남 시각").optional(),
                                        fieldWithPath("result.chatRoomList[].maxCount").type(NUMBER).description("채팅방의 최대 유저 수"),
                                        fieldWithPath("result.chatRoomList[].currentCount").type(NUMBER).description("채팅방의 현재 유저 수"),
                                        fieldWithPath("result.chatRoomList[].hashTagList").type(ARRAY).description("해시태그 리스트"),
                                        fieldWithPath("result.chatRoomList[].hashTagList[].id").type(NUMBER).description("해시태그 아이디").optional(),
                                        fieldWithPath("result.chatRoomList[].hashTagList[].content").type(STRING).description("해시태그 내용").optional()
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}/groups", 1)
                .then().log().all().statusCode(200);
    }

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