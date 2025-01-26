package com.coastee.server.global;

import com.coastee.server.login.LoginArgumentResolver;
import com.coastee.server.login.infrastructure.JwtProvider;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("test")
public abstract class ControllerTest {
    protected static final String ACCESS_TOKEN_HEADER = "Authorization";
    protected static final String ACCESS_TOKEN = "Bearer accessToken";
    protected static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    protected static final String REFRESH_TOKEN = "Bearer refreshToken";

    @LocalServerPort
    private int port;

    protected RequestSpecification spec;

    @MockitoBean
    protected JwtProvider jwtProvider;

    @MockitoBean
    protected LoginArgumentResolver loginArgumentResolver;

    @BeforeEach
    void setPort(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        spec = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint())
                ).build();
    }
}
