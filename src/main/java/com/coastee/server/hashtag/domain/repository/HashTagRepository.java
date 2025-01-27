package com.coastee.server.hashtag.domain.repository;

import com.coastee.server.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
}
