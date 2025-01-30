package com.coastee.server.hashtag.domain.repository;

import com.coastee.server.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    List<HashTag> findAllByContentIn(Set<String> contentSet);
}
