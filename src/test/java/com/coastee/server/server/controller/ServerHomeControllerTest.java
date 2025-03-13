package com.coastee.server.server.controller;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.chat.domain.reposistory.ChatRepository;
import com.coastee.server.chat.dto.ChatElement;
import com.coastee.server.chat.dto.ChatElements;
import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.chatroom.dto.ChatRoomDetailElement;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.fixture.*;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.hashtag.dto.HashTagElement;
import com.coastee.server.server.domain.Notice;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.NoticeRepository;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.server.dto.NoticeElement;
import com.coastee.server.server.dto.NoticeElements;
import com.coastee.server.server.dto.response.ServerHomeResponse;
import com.coastee.server.server.facade.ServerFacade;
import com.coastee.server.user.domain.User;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;


@DisplayName("서버 홈 조회 컨트롤러 테스트")
public class ServerHomeControllerTest extends ControllerTest {

    @MockitoBean
    private ServerFacade serverFacade;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ChatRepository chatRepository;

    @DisplayName("서버 홈 페이지를 조회한다.")
    @Test
    void getHome() throws Exception {
        // given
        Server server = serverRepository.save(ServerFixture.get());
        User owner = userRepository.save(UserFixture.get("owner"));
        List<HashTag> hashTagList = hashTagRepository.saveAll(HashTagFixture.getAll());
        List<ChatRoom> groupList = List.of(
                chatRoomRepository.save(ChatRoomFixture.getGroup(server, owner, "groupA")),
                chatRoomRepository.save(ChatRoomFixture.getGroup(server, owner, "groupB")),
                chatRoomRepository.save(ChatRoomFixture.getGroup(server, owner, "groupC"))
        );
        List<ChatRoom> meetingList = List.of(
                chatRoomRepository.save(ChatRoomFixture.getMeeting(server, owner, "meetingA")),
                chatRoomRepository.save(ChatRoomFixture.getMeeting(server, owner, "meetingB")),
                chatRoomRepository.save(ChatRoomFixture.getMeeting(server, owner, "meetingC"))
        );
        List<Notice> noticeList = noticeRepository.saveAll(NoticeFixture.getAll(server, owner));
        ChatRoom serverChatRoom = chatRoomRepository.save(ChatRoomFixture.getEntire(server));
        List<Chat> chatList = chatRepository.saveAll(ChatFixture.getAll(currentUser, serverChatRoom));

        when(serverFacade.getHomeWithConditions(any(), any(), anyString(), any())).thenReturn(
                new ServerHomeResponse(
                        hashTagList.stream().map(HashTagElement::new).toList(),
                        new ChatRoomElements(
                                new PageInfo(true, 0, 3, 3),
                                groupList.stream().map(ChatRoomDetailElement::new).toList()
                        ),
                        new ChatRoomElements(
                                new PageInfo(true, 0, 3, 3),
                                meetingList.stream().map(ChatRoomDetailElement::new).toList()
                        ),
                        new NoticeElements(
                                new PageInfo(true, 0, 3, 10),
                                noticeList.stream().map(NoticeElement::new).toList()
                        ),
                        new ChatElements(
                                new PageInfo(true, 0, 8, 10),
                                chatList.stream().map(ChatElement::new).toList()
                        )
                )
        );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("keyword", "검색하는키워드")
                .param("tagList", "#검색", "#해시태그")
                .filter(
                        document("get-server-home",
                                pathParameters(
                                        parameterWithName("serverId").description("서버 아이디")
                                ),
                                queryParameters(
                                        parameterWithName("keyword").description("검색 키워드"),
                                        parameterWithName("tagList").description("검색 해시태그")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.hashTagList").type(ARRAY).description("인기 해시태그 리스트"),
                                        fieldWithPath("result.hashTagList[].id").type(NUMBER).description("해시태그 아이디"),
                                        fieldWithPath("result.hashTagList[].content").type(STRING).description("해시태그 내용"),
                                        fieldWithPath("result.groupChatRoom").type(OBJECT).description("그룹챗"),
                                        fieldWithPath("result.groupChatRoom.pageInfo").type(OBJECT).description("그룹챗 페이징 정보"),
                                        fieldWithPath("result.groupChatRoom.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.groupChatRoom.pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                        fieldWithPath("result.groupChatRoom.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.groupChatRoom.pageInfo.size").type(NUMBER).description("페이지 크기"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList").type(ARRAY).description("그룹챗 리스트"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].id").type(NUMBER).description("그룹챗 아이디"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].thumbnail").type(STRING).description("썸네일").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].title").type(STRING).description("제목"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].content").type(STRING).description("내용"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].period").type(OBJECT).description("기간"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].period.startDate").type(ARRAY).description("시직날짜").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].period.endDate").type(ARRAY).description("끝날짜").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user").type(OBJECT).description("개설자"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.profileImage").type(STRING).description("프로필사진"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.userIntro").type(OBJECT).description("개설자 소개"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].user.userIntro.expYears").type(NUMBER).description("년차"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].address").type(OBJECT).description("주소"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].address.location").type(STRING).description("장소").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].address.details").type(STRING).description("설명").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].hasEntered").type(BOOLEAN).description("참여 여부 - 여기에서는 제공되지 않습니다. (null)").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].maxCount").type(NUMBER).description("최대 참여 인원"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].currentCount").type(NUMBER).description("현재 참여 인원"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].hashTagList").type(ARRAY).description("해시태그 리스트"),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].hashTagList[].id").type(NUMBER).description("해시태그 아이디").optional(),
                                        fieldWithPath("result.groupChatRoom.chatRoomList[].hashTagList[].content").type(STRING).description("내용").optional(),
                                        fieldWithPath("result.meetingChatRoom").type(OBJECT).description("커피챗"),
                                        fieldWithPath("result.meetingChatRoom.pageInfo").type(OBJECT).description("커피챗 페이징 정보"),
                                        fieldWithPath("result.meetingChatRoom.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.meetingChatRoom.pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                        fieldWithPath("result.meetingChatRoom.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.meetingChatRoom.pageInfo.size").type(NUMBER).description("페이지 크기"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList").type(ARRAY).description("커피챗 리스트"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].id").type(NUMBER).description("커피챗 아이디"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].thumbnail").type(STRING).description("썸네일").optional(),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].title").type(STRING).description("제목"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].content").type(STRING).description("내용"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].period").type(OBJECT).description("기간"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].period.startDate").type(ARRAY).description("시직날짜").optional(),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].period.endDate").type(ARRAY).description("끝날짜").optional(),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user").type(OBJECT).description("개설자"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.profileImage").type(STRING).description("프로필사진"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.userIntro").type(OBJECT).description("개설자 소개"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].user.userIntro.expYears").type(NUMBER).description("년차"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].address").type(OBJECT).description("주소"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].address.location").type(STRING).description("장소"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].address.details").type(STRING).description("설명"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].hasEntered").type(BOOLEAN).description("참여 여부 - 여기에서는 제공되지 않습니다. (null)").optional(),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].maxCount").type(NUMBER).description("최대 참여 인원"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].currentCount").type(NUMBER).description("현재 참여 인원"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].hashTagList").type(ARRAY).description("해시태그 리스트"),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].hashTagList[].id").type(NUMBER).description("해시태그 아이디").optional(),
                                        fieldWithPath("result.meetingChatRoom.chatRoomList[].hashTagList[].content").type(STRING).description("내용").optional(),
                                        fieldWithPath("result.notice").type(OBJECT).description("전체 채팅방 공지"),
                                        fieldWithPath("result.notice.pageInfo").type(OBJECT).description("공지 페이징 정보"),
                                        fieldWithPath("result.notice.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.notice.pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                        fieldWithPath("result.notice.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.notice.pageInfo.size").type(NUMBER).description("페이지 크기"),
                                        fieldWithPath("result.notice.noticeList").type(ARRAY).description("공지 리스트"),
                                        fieldWithPath("result.notice.noticeList[].id").type(NUMBER).description("공지 아이디"),
                                        fieldWithPath("result.notice.noticeList[].title").type(STRING).description("공지 제목"),
                                        fieldWithPath("result.notice.noticeList[].content").type(STRING).description("공지 내용"),
                                        fieldWithPath("result.chat").type(OBJECT).description("전체 채팅"),
                                        fieldWithPath("result.chat.pageInfo").type(OBJECT).description("채팅 페이징 정보"),
                                        fieldWithPath("result.chat.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.chat.pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                        fieldWithPath("result.chat.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.chat.pageInfo.size").type(NUMBER).description("페이지 크기"),
                                        fieldWithPath("result.chat.chatList").type(ARRAY).description("채팅 리스트"),
                                        fieldWithPath("result.chat.chatList[].id").type(NUMBER).description("채팅 아이디"),
                                        fieldWithPath("result.chat.chatList[].user").type(OBJECT).description("작성자"),
                                        fieldWithPath("result.chat.chatList[].user.id").type(NUMBER).description("작성자 아이디"),
                                        fieldWithPath("result.chat.chatList[].user.profileImage").type(STRING).description("프로필사진"),
                                        fieldWithPath("result.chat.chatList[].user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.chat.chatList[].user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.chat.chatList[].user.userIntro").type(OBJECT).description("작성자 소개"),
                                        fieldWithPath("result.chat.chatList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.chat.chatList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.chat.chatList[].user.userIntro.expYears").type(NUMBER).description("년차"),
                                        fieldWithPath("result.chat.chatList[].content").type(STRING).description("내용"),
                                        fieldWithPath("result.chat.chatList[].createdDate").type(ARRAY).description("보낸 시각"),
                                        fieldWithPath("result.chat.chatList[].type").type(STRING).description("채팅 타입"),
                                        fieldWithPath("result.chat.chatList[].chatRoomId").type(NUMBER).description("채팅방아이디")
                                )
                        ))
                .when().get("/api/v1/servers/{serverId}", server.getId())
                .then().log().all().statusCode(200);
    }
}
