package com.coastee.server.global;

import com.coastee.server.auth.UserOnlyChecker;
import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.auth.domain.Authority;
import com.coastee.server.fixture.UserFixture;
import com.coastee.server.login.LoginArgumentResolver;
import com.coastee.server.login.infrastructure.JwtProvider;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import io.jsonwebtoken.impl.DefaultClaims;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static com.coastee.server.global.domain.Constant.AUTHORITIES_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
    protected RequestSpecification spec;
    protected User currentUser;

    @LocalServerPort
    private int port;

    @MockitoBean
    protected JwtProvider jwtProvider;

    @MockitoBean
    protected LoginArgumentResolver loginArgumentResolver;

    @MockitoBean
    private UserOnlyChecker userOnlyChecker;

    @Autowired
    private UserRepository userRepository;

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
        currentUser = userRepository.save(UserFixture.get());
        jwtSettings(currentUser);
    }

    private void jwtSettings(final User user) {
        given(jwtProvider.validateAccessToken(any())).willReturn(true);
        doNothing().when(jwtProvider).validateRefreshToken(any());
        given(jwtProvider.getSubject(any())).willReturn(user.getId().toString());
        given(loginArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(Accessor.user(user.getId()));

        // role
        HashMap<String, Object> map = new HashMap<>();
        map.put(AUTHORITIES_KEY, Authority.USER);
        given(jwtProvider.getTokenClaims(any())).willReturn(new DefaultClaims(map));

        // checker
        doNothing().when(userOnlyChecker).check(any());
    }
}
