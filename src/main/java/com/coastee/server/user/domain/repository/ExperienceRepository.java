package com.coastee.server.user.domain.repository;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Page<Experience> findAllByUser(final User user, final Pageable pageable);
}
