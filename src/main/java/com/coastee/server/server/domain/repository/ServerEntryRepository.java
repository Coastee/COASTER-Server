package com.coastee.server.server.domain.repository;

import com.coastee.server.global.BaseEntityStatus;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerEntryRepository extends JpaRepository<ServerEntry, Long> {
    Optional<ServerEntry> findByUserAndServerAndStatus(final User user, final Server server, final BaseEntityStatus status);
}
