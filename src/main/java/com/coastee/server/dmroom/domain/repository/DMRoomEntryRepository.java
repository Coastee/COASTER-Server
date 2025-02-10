package com.coastee.server.dmroom.domain.repository;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.DirectMessageRoomEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DMRoomEntryRepository extends JpaRepository<DirectMessageRoomEntry, Long> {

    Optional<DirectMessageRoomEntry> findByUserAndDirectMessageRoom(
            final User user,
            final DirectMessageRoom directMessageRoom
    );
}
