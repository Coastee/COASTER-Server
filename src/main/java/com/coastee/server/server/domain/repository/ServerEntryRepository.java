package com.coastee.server.server.domain.repository;

import com.coastee.server.global.domain.BaseEntityStatus;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.ServerEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerEntryRepository extends JpaRepository<ServerEntry, Long> {

    Optional<ServerEntry> findByUserAndServer(final User user, final Server server);

    @EntityGraph(attributePaths = {"server"})
    Optional<ServerEntry> findFirstByUserAndStatus(final User user, final BaseEntityStatus status);

    @EntityGraph(attributePaths = {"server"})
    List<ServerEntry> findByUserAndStatus(final User user, final BaseEntityStatus baseEntityStatus);

    @EntityGraph(attributePaths = {"server"})
    List<ServerEntry> findByUserAndServerIn(
            final User user,
            final List<Server> serverList
    );
}
