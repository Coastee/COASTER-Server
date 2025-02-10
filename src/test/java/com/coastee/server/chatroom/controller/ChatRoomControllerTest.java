package com.coastee.server.chatroom.controller;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.reposistory.ChatRepository;
import com.coastee.server.chat.dto.ChatElement;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomTag;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.chatroom.domain.repository.ChatRoomTagRepository;
import com.coastee.server.chatroom.dto.ChatRoomDetailElement;
import com.coastee.server.chatroom.dto.ChatRoomElement;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.facade.ChatRoomFacade;
import com.coastee.server.fixture.ChatFixture;
import com.coastee.server.fixture.HashTagFixture;
import com.coastee.server.fixture.ServerFixture;
import com.coastee.server.fixture.UserFixture;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.UserElement;
import com.coastee.server.user.dto.UserElements;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.stream.Stream;

import static com.coastee.server.fixture.ChatRoomFixture.getMeeting;
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

@DisplayName("채팅방 컨트롤러 테스트")
class ChatRoomControllerTest extends ControllerTest {
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

    @DisplayName("모든 채팅방을 찾는다.")
    @Test
    void findAll() throws Exception {
        // given
        User userA = userRepository.save(UserFixture.get("userA"));
        User userB = userRepository.save(UserFixture.get("userB"));
        User userC = userRepository.save(UserFixture.get("userC"));
        Server server = serverRepository.save(ServerFixture.get());
        List<ChatRoom> chatRoomList = List.of(
                chatRoomRepository.save(getMeeting(server, userA)),
                chatRoomRepository.save(getMeeting(server, userB)),
                chatRoomRepository.save(getMeeting(server, userC))
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

        when(chatRoomFacade.findByScope(any(), any(), any(), any(), any()))
                .thenReturn(
                        new ChatRoomElements(
                                new PageInfo(true, 0, 3, 40),
                                chatRoomList.stream().map(chatRoom -> new ChatRoomDetailElement(
                                        chatRoom, false
                                )).toList()
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
                        document("find-all-chatroom",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗")

                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)"),
                                        parameterWithName("sort")
                                                .description("정렬기준 - `name` : 이름순, `remain` : 마감임박순 (default: 최신순)"),
                                        parameterWithName("scope")
                                                .description("조회 기준 - `joined` : 참여한 채팅방 조회, `owner` : 개설한 채팅방 조회 (default: 전체 조회)")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
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
                                        fieldWithPath("result.chatRoomList[].period").type(OBJECT).description("기간").optional(),
                                        fieldWithPath("result.chatRoomList[].period.startDate").type(ARRAY).description("시작 시간").optional(),
                                        fieldWithPath("result.chatRoomList[].period.endDate").type(ARRAY).description("종료 시간").optional(),
                                        fieldWithPath("result.chatRoomList[].user").type(OBJECT).description("채팅방 개설자"),
                                        fieldWithPath("result.chatRoomList[].user.id").type(NUMBER).description("개설자 아이디"),
                                        fieldWithPath("result.chatRoomList[].user.profileImage").type(STRING).description("개설자 프로필 사진"),
                                        fieldWithPath("result.chatRoomList[].user.nickname").type(STRING).description("개설자 닉네임"),
                                        fieldWithPath("result.chatRoomList[].user.linkedInVerify").type(BOOLEAN).description("개설자 링크드인 인증 여부"),
                                        fieldWithPath("result.chatRoomList[].user.userIntro").type(OBJECT).description("개설자 소개"),
                                        fieldWithPath("result.chatRoomList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.chatRoomList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.chatRoomList[].user.userIntro.expYears").type(NUMBER).description("경력 년차"),
                                        fieldWithPath("result.chatRoomList[].address").type(OBJECT).description("주소").optional(),
                                        fieldWithPath("result.chatRoomList[].address.location").type(STRING).description("장소").optional(),
                                        fieldWithPath("result.chatRoomList[].address.details").type(STRING).description("상세 설명").optional(),
                                        fieldWithPath("result.chatRoomList[].hasEntered").type(BOOLEAN).description("현재 유저가 해당 채팅방에 이미 참여하였는지에 대한 여부"),
                                        fieldWithPath("result.chatRoomList[].maxCount").type(NUMBER).description("채팅방의 최대 유저 수"),
                                        fieldWithPath("result.chatRoomList[].currentCount").type(NUMBER).description("채팅방의 현재 유저 수"),
                                        fieldWithPath("result.chatRoomList[].hashTagList").type(ARRAY).description("해시태그 리스트"),
                                        fieldWithPath("result.chatRoomList[].hashTagList[].id").type(NUMBER).description("해시태그 아이디").optional(),
                                        fieldWithPath("result.chatRoomList[].hashTagList[].content").type(STRING).description("해시태그 내용").optional()
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}/{chatRoomType}", 1, "groups")
                .then().log().all().statusCode(200);
    }

    @DisplayName("개설한 채팅방을 찾는다.")
    @Test
    void findByOwner() throws Exception {
        // given
        User userA = userRepository.save(UserFixture.get("userA"));
        User userB = userRepository.save(UserFixture.get("userB"));
        User userC = userRepository.save(UserFixture.get("userC"));
        Server server = serverRepository.save(ServerFixture.get());
        List<ChatRoom> chatRoomList = List.of(
                chatRoomRepository.save(ChatRoom.groupChatRoom(server, userA, "titleA", "contentA")),
                chatRoomRepository.save(ChatRoom.groupChatRoom(server, userB, "titleB", "contentB")),
                chatRoomRepository.save(ChatRoom.groupChatRoom(server, userC, "titleC", "contentC"))
        );

        when(chatRoomFacade.findByScope(any(), any(), any(), any(), any()))
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
                .param("scope", "owner")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("find-owner-chatroom",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗")
                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)"),
                                        parameterWithName("sort")
                                                .description("정렬기준 - `name` : 이름순, `remain` : 마감임박순 (default: 최신순)"),
                                        parameterWithName("scope")
                                                .description("조회 기준 - `joined` : 참여한 채팅방 조회, `owner` : 개설한 채팅방 조회 (default: 전체 조회)")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
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
                                        fieldWithPath("result.chatRoomList[].period").type(OBJECT).description("기간").optional(),
                                        fieldWithPath("result.chatRoomList[].period.startDate").type(STRING).description("시작 시간").optional(),
                                        fieldWithPath("result.chatRoomList[].period.endDate").type(STRING).description("종료 시간").optional()
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}/{chatRoomType}", 1, "groups")
                .then().log().all().statusCode(200);
    }

    @DisplayName("채팅방 참여자를 조회한다.")
    @Test
    void getParticipants() throws Exception {
        // given
        User userA = userRepository.save(UserFixture.get("userA"));
        User userB = userRepository.save(UserFixture.get("userB"));
        User userC = userRepository.save(UserFixture.get("userC"));
        Server server = serverRepository.save(ServerFixture.get());
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.groupChatRoom(server, userA, "titleA", "contentA"));

        when(chatRoomFacade.getParticipants(any(), any(), any()))
                .thenReturn(new UserElements(
                                new PageInfo(true, 0, 3, 40),
                                Stream.of(userA, userB, userC).map(UserElement::new).toList()
                        )
                );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("get-participants",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗"),
                                        parameterWithName("chatRoomId").description("채팅방 아이디")
                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
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
                                        fieldWithPath("result.userList").type(ARRAY).description("유저 리스트"),
                                        fieldWithPath("result.userList[].id").type(NUMBER).description("유저 아이디"),
                                        fieldWithPath("result.userList[].profileImage").type(STRING).description("유저 프로필이미지").optional(),
                                        fieldWithPath("result.userList[].nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.userList[].linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.userList[].userIntro").type(OBJECT).description("소개").optional(),
                                        fieldWithPath("result.userList[].userIntro.headline").type(STRING).description("한줄소개").optional(),
                                        fieldWithPath("result.userList[].userIntro.job").type(STRING).description("직업").optional(),
                                        fieldWithPath("result.userList[].userIntro.expYears").type(NUMBER).description("년차").optional()
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}/{chatRoomType}/{chatRoomId}/users", 1, "groups", chatRoom.getId())
                .then().log().all().statusCode(200);
    }

    @DisplayName("채팅 이력을 조회한다.")
    @Test
    void getChats() throws Exception {
        // given
        User user = userRepository.save(UserFixture.get());
        Server server = serverRepository.save(ServerFixture.get());
        ChatRoom chatRoom = chatRoomRepository.save(getMeeting(server, user));
        List<Chat> chatList = List.of(
                chatRepository.save(ChatFixture.get(user, chatRoom)),
                chatRepository.save(ChatFixture.get(user, chatRoom)),
                chatRepository.save(ChatFixture.get(user, chatRoom))
        );

        when(chatRoomFacade.getChats(any(), any(), any())).thenReturn(
                new ChatElements(
                        new PageInfo(true, 0, 3, 40),
                        chatList.stream().map(ChatElement::new).toList()
                )
        );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("find-chats",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗"),
                                        parameterWithName("chatRoomId").description("채팅방 아이디")
                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
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
                                        fieldWithPath("result.chatList").type(ARRAY).description("채팅 리스트"),
                                        fieldWithPath("result.chatList[].id").type(NUMBER).description("채팅 아이디"),
                                        fieldWithPath("result.chatList[].user").type(OBJECT).description("유저"),
                                        fieldWithPath("result.chatList[].user.id").type(NUMBER).description("유저 아이디"),
                                        fieldWithPath("result.chatList[].user.profileImage").type(STRING).description("프로필 사진"),
                                        fieldWithPath("result.chatList[].user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.chatList[].user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.chatList[].user.userIntro").type(OBJECT).description("소개"),
                                        fieldWithPath("result.chatList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.chatList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.chatList[].user.userIntro.expYears").type(NUMBER).description("경력 년차"),
                                        fieldWithPath("result.chatList[].content").type(STRING).description("채팅 내용"),
                                        fieldWithPath("result.chatList[].createdDate").type(ARRAY).description("전송 시간"),
                                        fieldWithPath("result.chatList[].type").type(STRING)
                                                .description("타입 - `ENTER` : 참여, `QUIT` : 탈퇴, `TALK` : 대화, `DELETE` : 삭제")
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}/{chatRoomType}/{chatRoomId}", 1, "meetings", 1)
                .then().log().all().statusCode(200);
    }

    @DisplayName("채팅방에 참여한다.")
    @Test
    void enter() throws Exception {
        // given
        doNothing().when(chatRoomFacade).enter(any(), any());

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("enter-chatroom",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗"),
                                        parameterWithName("chatRoomId").description("채팅방 아이디")
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
                .when().post("/api/v1/servers/{serverId}/{chatRoomType}/{chatRoomId}", 1, "meetings", 2)
                .then().log().all().statusCode(200);
    }

    @DisplayName("채팅방을 탈퇴한다.")
    @Test
    void exit() throws Exception {
        // given
        doNothing().when(chatRoomFacade).exit(any(), any());

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("exit-chatroom",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디"),
                                        parameterWithName("chatRoomType").description("채팅방 타입 - `groups` : 그룹챗, `meetings` 커피챗"),
                                        parameterWithName("chatRoomId").description("채팅방 아이디")
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
                .when().delete("/api/v1/servers/{serverId}/{chatRoomType}/{chatRoomId}", 1, "meetings", 2)
                .then().log().all().statusCode(200);
    }

}