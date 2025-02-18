package com.coastee.server.dm.domain.repository;

import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.dmroom.domain.DirectMessageRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMRepository extends JpaRepository<DirectMessage, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<DirectMessage> findAllByDirectMessageRoom(
            final DirectMessageRoom dmRoom,
            final Pageable pageable
    );
}
