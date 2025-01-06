package com.coastee.server.community.domain.repository;

import com.coastee.server.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
