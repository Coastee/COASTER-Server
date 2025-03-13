package com.coastee.server.dmroom.domain.repository;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.DirectMessageRoomEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DMRoomEntryRepository extends JpaRepository<DirectMessageRoomEntry, Long> {

    Optional<DirectMessageRoomEntry> findByUserAndDirectMessageRoom(
            final User user,
            final DirectMessageRoom directMessageRoom
    );

    @Query("""
            select e from DirectMessageRoomEntry e
            join fetch e.user
            where e.directMessageRoom = :dmRoom
            and e.user != :user
            """)
    Optional<DirectMessageRoomEntry> findAnotherEntryByUserAndDirectMessageRoom(
            @Param("user") final User user,
            @Param("dmRoom") final DirectMessageRoom dmRoom
    );
}
