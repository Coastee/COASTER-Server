package com.coastee.server.user.controller;

import com.coastee.server.user.dto.request.ExperienceCreateRequest;
import com.coastee.server.user.dto.request.ExperienceUpdateRequest;
import com.coastee.server.user.facade.ExperienceFacade;
import com.coastee.server.user.facade.UserFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("경력 컨트롤러 테스트")
class ExperienceControllerTest extends ControllerTest {

    @MockitoBean
    private ExperienceFacade experienceFacade;

    @MockitoBean
    private UserFacade userFacade;

    @DisplayName("경력을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        doNothing().when(userFacade).validateAccess(any(), anyLong());
        doNothing().when(experienceFacade).create(anyLong(), any());

        ExperienceCreateRequest request = new ExperienceCreateRequest(
                "title",
                List.of("000 개발에 참여하였습니다.", "000 결과에 기여하였습니다."),
                LocalDateTime.now().minusMonths(5),
                LocalDateTime.now()
        );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .filter(
                        document("create-experience",
                                pathParameters(
                                        parameterWithName("userId").description("유저 아이디")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("contentList").type(ARRAY).description("내용 리스트"),
                                        fieldWithPath("startDate").type(ARRAY).description("시작기간"),
                                        fieldWithPath("endDate").type(ARRAY).description("끝나는기간 : null로 전달시 현재까지 진행되는 경력으로 간주됨.")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/users/{userId}/experiences", currentUser.getId())
                .then().log().all().statusCode(200);
    }

    @DisplayName("경력을 수정한다.")
    @Test
    void update() throws Exception {
        // given
        doNothing().when(userFacade).validateAccess(any(), anyLong());
        doNothing().when(experienceFacade).update(anyLong(), anyLong(), any());

        ExperienceUpdateRequest request = new ExperienceUpdateRequest(
                "title",
                List.of("000 개발에 참여하였습니다.", "000 결과에 기여하였습니다."),
                LocalDateTime.now().minusMonths(5),
                LocalDateTime.now()
        );

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .filter(
                        document("update-experience",
                                pathParameters(
                                        parameterWithName("userId").description("유저 아이디"),
                                        parameterWithName("experienceId").description("수정할 경력 아이디")
                                ),
                                requestHeaders(
                                        headerWithName(ACCESS_TOKEN_HEADER).description("액세스 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("contentList").type(ARRAY).description("내용"),
                                        fieldWithPath("startDate").type(ARRAY).description("시작기간"),
                                        fieldWithPath("endDate").type(ARRAY).description("끝나는기간 : null로 전달시 현재까지 진행되는 경력으로 간주됨.")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지")
                                )
                        ))
                .when().post("/api/v1/users/{userId}/experiences/{experienceId}", currentUser.getId(), 35)
                .then().log().all().statusCode(200);
    }

    @DisplayName("경력을 삭제한다.")
    @Test
    void delete() throws Exception {
        // given
        doNothing().when(userFacade).validateAccess(any(), anyLong());
        doNothing().when(experienceFacade).delete(anyLong(), anyLong());

        // when & then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("delete-experience",
                                pathParameters(
                                        parameterWithName("userId").description("유저 아이디"),
                                        parameterWithName("experienceId").description("삭제할 경력 아이디")
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
                .when().delete("/api/v1/users/{userId}/experiences/{experienceId}", currentUser.getId(), 35)
                .then().log().all().statusCode(200);
    }
}