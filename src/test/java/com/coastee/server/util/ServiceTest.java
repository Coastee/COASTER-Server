package com.coastee.server.util;

import com.coastee.server.fixture.UserFixture;
import com.coastee.server.image.service.BlobStorageService;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public abstract class ServiceTest {
    protected User currentUser;

    @Autowired
    protected UserRepository userRepository;

    @MockitoBean
    protected BlobStorageService blobStorageService;

    @BeforeEach
    void setUp() {
        currentUser = userRepository.save(UserFixture.get());
        when(blobStorageService.upload(any(), any(), any())).thenReturn("");
    }
}
