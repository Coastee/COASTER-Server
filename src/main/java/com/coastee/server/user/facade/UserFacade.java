package com.coastee.server.user.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.image.domain.DirName;
import com.coastee.server.image.service.BlobStorageService;
import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.UserDetailElement;
import com.coastee.server.user.dto.request.UserUpdateRequest;
import com.coastee.server.user.service.ExperienceService;
import com.coastee.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus._INVALID_AUTHORITY;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final ExperienceService experienceService;
    private final BlobStorageService blobStorageService;

    public UserDetailElement getProfile(final Long userId, final Pageable pageable) {
        User user = userService.findById(userId);
        Page<Experience> experiencePage = experienceService.findAllByUser(user, pageable);
        return UserDetailElement.from()
                .user(user)
                .experiencePage(experiencePage)
                .build();
    }

    @Transactional
    public void update(
            final Long userId,
            final UserUpdateRequest request,
            final MultipartFile image
    ) {
        User user = userService.findById(userId);
        userService.update(user, request);
        if (image != null && !image.isEmpty()) {
            String imageUrl = blobStorageService.upload(image, DirName.USER, user.getId());
            user.updateProfileImage(imageUrl);
        }
    }

    public void validateAccess(final Accessor accessor, final Long userId) {
        if (accessor.getUserId().equals(userId)) return;
        throw new GeneralException(_INVALID_AUTHORITY);
    }
}
