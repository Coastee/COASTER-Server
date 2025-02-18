package com.coastee.server.dmroom.controller;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dm.domain.repository.DMRepository;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.repository.DMRoomRepository;
import com.coastee.server.dmroom.domain.repository.dto.FindAnotherUserAndRecentDM;
import com.coastee.server.dmroom.dto.DMRoomElement;
import com.coastee.server.dmroom.dto.DMRoomElements;
import com.coastee.server.dmroom.facade.DMRoomFacade;
import com.coastee.server.fixture.DMFixture;
import com.coastee.server.fixture.DMRoomFixture;
import com.coastee.server.fixture.UserFixture;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.user.domain.User;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("DM방 컨트롤러 테스트")
class DMRoomControllerTest extends ControllerTest {
    @MockitoBean
    private DMRoomFacade dmRoomFacade;

    @Autowired
    private DMRoomRepository dmRoomRepository;

    @Autowired
    private DMRepository dmRepository;

    @DisplayName("내가 참여한 DMroom을 조회한다.")
    @Test
    void getRooms() throws Exception {
        // given
        User userA = userRepository.save(UserFixture.get("userA"));
        User userB = userRepository.save(UserFixture.get("userB"));
        User userC = userRepository.save(UserFixture.get("userC"));
        DirectMessageRoom roomA = dmRoomRepository.save(DMRoomFixture.get(currentUser));
        DirectMessageRoom roomB = dmRoomRepository.save(DMRoomFixture.get(currentUser));
        DirectMessageRoom roomC = dmRoomRepository.save(DMRoomFixture.get(currentUser));
        DirectMessage dmA = dmRepository.save(DMFixture.get(userA, roomA));
        DirectMessage dmB = dmRepository.save(DMFixture.get(userB, roomB));
        DirectMessage dmC = dmRepository.save(DMFixture.get(userC, roomC));

        List<DirectMessageRoom> dmRoomList = List.of(roomA, roomB, roomC);
        Map<Long, FindAnotherUserAndRecentDM> dmRoomMap = Map.of(
                roomA.getId(), new FindAnotherUserAndRecentDM(roomA.getId(), userA, dmA),
                roomB.getId(), new FindAnotherUserAndRecentDM(roomB.getId(), userB, dmB),
                roomC.getId(), new FindAnotherUserAndRecentDM(roomC.getId(), userC, dmC)
        );

        when(dmRoomFacade.getRooms(any(), any()))
                .thenReturn(
                        new DMRoomElements(
                                new PageInfo(true, 0, 3, 40),
                                dmRoomList.stream().map(
                                        r -> {
                                            FindAnotherUserAndRecentDM dto = dmRoomMap.get(r.getId());
                                            return new DMRoomElement(r, dto.getUser(), dto.getDm());
                                        }
                                ).toList()
                        )
                );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("find-all-dmroom",
                                pathParameters(
                                        parameterWithName("userId").description("유저 아이디")
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
                                        fieldWithPath("result.dmRoomList").type(ARRAY).description("DM방 리스트"),
                                        fieldWithPath("result.dmRoomList[].id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.dmRoomList[].user").type(OBJECT).description("나와 dm을 나눈 유저"),
                                        fieldWithPath("result.dmRoomList[].user.id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.dmRoomList[].user.profileImage").type(STRING).description("프로필 사진"),
                                        fieldWithPath("result.dmRoomList[].user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.dmRoomList[].user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.dmRoomList[].user.userIntro").type(OBJECT).description("소개"),
                                        fieldWithPath("result.dmRoomList[].user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.dmRoomList[].user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.dmRoomList[].user.userIntro.expYears").type(NUMBER).description("경력 년차"),
                                        fieldWithPath("result.dmRoomList[].dm").type(OBJECT).description("최근에 보낸 DM"),
                                        fieldWithPath("result.dmRoomList[].dm.id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.dmRoomList[].dm.user").type(OBJECT).description("전송자"),
                                        fieldWithPath("result.dmRoomList[].dm.user.id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.dmRoomList[].dm.user.profileImage").type(STRING).description("프로필 사진"),
                                        fieldWithPath("result.dmRoomList[].dm.user.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.dmRoomList[].dm.user.linkedInVerify").type(BOOLEAN).description("링크드인 인증 여부"),
                                        fieldWithPath("result.dmRoomList[].dm.user.userIntro").type(OBJECT).description("소개"),
                                        fieldWithPath("result.dmRoomList[].dm.user.userIntro.headline").type(STRING).description("한줄소개"),
                                        fieldWithPath("result.dmRoomList[].dm.user.userIntro.job").type(STRING).description("직업"),
                                        fieldWithPath("result.dmRoomList[].dm.user.userIntro.expYears").type(NUMBER).description("경력 년차"),
                                        fieldWithPath("result.dmRoomList[].dm.content").type(STRING).description("내용"),
                                        fieldWithPath("result.dmRoomList[].dm.createdDate").type(ARRAY).description("전송시간"),
                                        fieldWithPath("result.dmRoomList[].dm.type").type(STRING).description("타입"),
                                        fieldWithPath("result.dmRoomList[].dm.dmRoomId").type(NUMBER).description("DM의 채팅방 아이디")
                                )
                        ))
                .when().get("/api/v1/users/{userId}/dms", 1)
                .then().log().all().statusCode(200);
    }
}