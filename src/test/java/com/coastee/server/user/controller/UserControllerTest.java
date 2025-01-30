package com.coastee.server.user.controller;

import com.coastee.server.fixture.ExperienceFixture;
import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.repository.ExperienceRepository;
import com.coastee.server.user.dto.UserDetailElement;
import com.coastee.server.user.facade.UserFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.coastee.server.global.domain.Constant.DEFAULT_PAGING_SIZE;
import static com.coastee.server.global.util.ListToPageConverter.toPage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("유저 controller 테스트")
class UserControllerTest extends ControllerTest {

    @Autowired
    private ExperienceRepository experienceRepository;

    @MockitoBean
    private UserFacade userFacade;

    @DisplayName("유저 프로필 조회")
    @Test
    void getProfile() throws Exception {
        // given
        List<Experience> experienceList = experienceRepository.saveAll(ExperienceFixture.getAll(currentUser));
        when(userFacade.getProfile(any(), any())).thenReturn(
                UserDetailElement.from()
                        .user(currentUser)
                        .experiencePage(toPage(experienceList, PageRequest.of(0, DEFAULT_PAGING_SIZE)))
                        .build()
        );

        // when & then
        RestAssured.given(spec).log().all()
                .contentType(ContentType.JSON)
                .param("page", "0")
                .filter(
                        document("get-profile",
                                pathParameters(
                                        parameterWithName("userId").description("유저 아이디")
                                ),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호 (default: 0)")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").type(BOOLEAN).description("성공 여부"),
                                        fieldWithPath("code").type(STRING).description("결과 코드"),
                                        fieldWithPath("message").type(STRING).description("결과 메세지"),
                                        fieldWithPath("result").type(OBJECT).description("결과 데이터"),
                                        fieldWithPath("result.id").type(NUMBER).description("유저 아이디"),
                                        fieldWithPath("result.profileImage").type(STRING).description("프로필 사진"),
                                        fieldWithPath("result.nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("result.headline").type(STRING).description("헤드라인"),
                                        fieldWithPath("result.bio").type(STRING).description("헤드라인"),
                                        fieldWithPath("result.urlList").type(ARRAY).description("URL 리스트"),
                                        fieldWithPath("result.experience").type(OBJECT).description("경력"),
                                        fieldWithPath("result.experience.pageInfo").type(OBJECT).description("페이징 정보"),
                                        fieldWithPath("result.experience.pageInfo.lastPage").type(BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("result.experience.pageInfo.totalPages").type(NUMBER).description("총 페이지 개수"),
                                        fieldWithPath("result.experience.pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                                        fieldWithPath("result.experience.pageInfo.size").type(NUMBER).description("페이지 사이즈"),
                                        fieldWithPath("result.experience.experienceList").type(ARRAY).description("경력 리스트"),
                                        fieldWithPath("result.experience.experienceList[].id").type(NUMBER).description("경력 아이디"),
                                        fieldWithPath("result.experience.experienceList[].title").type(STRING).description("제목"),
                                        fieldWithPath("result.experience.experienceList[].content").type(STRING).description("상세 내용"),
                                        fieldWithPath("result.experience.experienceList[].startDate").type(ARRAY).description("시작날짜"),
                                        fieldWithPath("result.experience.experienceList[].endDate").type(ARRAY).description("종료날짜")
                                )
                        ))
                .when().get("/api/v1/users/{userId}", currentUser.getId())
                .then().log().all().statusCode(200);
    }
}