package com.coastee.server.hashtag.controller;

import com.coastee.server.fixture.HashTagFixture;
import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.hashtag.dto.HashTagElement;
import com.coastee.server.hashtag.dto.HashTagElements;
import com.coastee.server.hashtag.facade.HashTagFacade;
import com.coastee.server.util.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@DisplayName("해시태그 컨트롤러 테스트")
class HashTagControllerTest extends ControllerTest {

    @MockitoBean
    private HashTagFacade hashTagFacade;

    @Autowired
    private HashTagRepository hashTagRepository;

    @DisplayName("해시태그가 검색된다.")
    @Test
    void findAll() throws Exception {
        // given
        List<HashTag> hashTagList = hashTagRepository
                .saveAll(HashTagFixture.getAll());

        // when
        when(hashTagFacade.findWithConditions(any(), any(), any()))
                .thenReturn(
                        new HashTagElements(
                                new PageInfo(true, 0, 3, 40),
                                hashTagList.stream().map(HashTagElement::new).toList()
                        )
                );

        // then
        RestAssured.given(spec).log().all()
                .header(ACCESS_TOKEN_HEADER, ACCESS_TOKEN)
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document("find-all-dmroom",
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
                                        fieldWithPath("result.hashTagList").type(ARRAY).description("DM방 리스트"),
                                        fieldWithPath("result.hashTagList[].id").type(NUMBER).description("아이디"),
                                        fieldWithPath("result.hashTagList[].content").type(OBJECT).description("나와 dm을 나눈 유저")
                                )
                        ))
                .when().get("/api/v1/dms")
                .then().log().all().statusCode(200);
    }
}