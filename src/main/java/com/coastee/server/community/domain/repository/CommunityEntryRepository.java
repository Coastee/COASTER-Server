package com.coastee.server.community.domain.repository;

import com.coastee.server.community.domain.CommunityEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityEntryRepository extends JpaRepository<CommunityEntry, Long> {
}
