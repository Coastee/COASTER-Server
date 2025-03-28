package com.coastee.server.user.domain.repository;

import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(final String socialId);

    Optional<User> findByRefreshToken(final String refreshToken);
}
