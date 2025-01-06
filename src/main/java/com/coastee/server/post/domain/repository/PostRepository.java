package com.coastee.server.post.domain.repository;

import com.coastee.server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
