package com.coastee.server.user.service;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.domain.repository.ExperienceRepository;
import com.coastee.server.user.dto.request.ExperienceUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.INVALID_EXPERIENCE_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;

    public Experience findById(final Long id) {
        return experienceRepository.findById(id).orElseThrow(() -> new GeneralException(INVALID_EXPERIENCE_ID));
    }

    public Page<Experience> findAllByUser(final User user, final Pageable pageable) {
        return experienceRepository.findAllByUser(user, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "period.endDate"))
        );
    }

    @Transactional
    public Experience save(final Experience experience) {
        return experienceRepository.save(experience);
    }

    @Transactional
    public void update(
            final Experience experience,
            final ExperienceUpdateRequest request
    ) {
        experience.updateTitle(request.getTitle());
        experience.updateContentList(request.getContentList());
        experience.updatePeriod(request.getStartDate(), request.getEndDate());
    }
}
