package com.coastee.server.user.controller;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import com.coastee.server.fixture.ServerFixture;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.user.dto.response.ScheduleElement;
import com.coastee.server.user.dto.response.ScheduleElements;
import com.coastee.server.user.facade.ScheduleFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.coastee.server.fixture.ChatRoomFixture.getMeeting;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("스케줄 컨트롤러 테스트")
class ScheduleControllerTest extends ControllerTest {

    @MockitoBean
    private ScheduleFacade scheduleFacade;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ServerRepository serverRepository;

    @DisplayName("스케줄이 조회된다.")
    @Test
    void getSchedule() throws Exception {
        // given
        Server server = serverRepository.save(ServerFixture.get());
        List<ChatRoom> chatRoomList = List.of(
                chatRoomRepository.save(getMeeting(server, currentUser, "titleA")),
                chatRoomRepository.save(getMeeting(server, currentUser, "titleB")),
                chatRoomRepository.save(getMeeting(server, currentUser, "titleC"))
        );

        // when
        when(scheduleFacade.getSchedule(any(), any()))
                .thenReturn(
                        new ScheduleElements(
                                new PageInfo(true, 0, 3, 40),
                                chatRoomList.stream().map(ScheduleElement::new).toList()
                        )
                );

        // then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("get-schedule",
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
                                        fieldWithPath("result.scheduleList").type(ARRAY).description("스케줄 리스트"),
                                        fieldWithPath("result.scheduleList[].id").type(NUMBER).description("채팅방 아이디"),
                                        fieldWithPath("result.scheduleList[].title").type(STRING).description("채팅방 제목"),
                                        fieldWithPath("result.scheduleList[].period").type(OBJECT).description("기간").optional(),
                                        fieldWithPath("result.scheduleList[].period.startDate").type(ARRAY).description("시작 시간").optional(),
                                        fieldWithPath("result.scheduleList[].period.endDate").type(ARRAY).description("종료 시간").optional(),
                                        fieldWithPath("result.scheduleList[].address").type(OBJECT).description("주소").optional(),
                                        fieldWithPath("result.scheduleList[].address.location").type(STRING).description("장소").optional(),
                                        fieldWithPath("result.scheduleList[].address.details").type(STRING).description("상세 설명").optional()
                                )
                        ))
                .when().get("/api/v1/schedules")
                .then().log().all().statusCode(200);
    }

}