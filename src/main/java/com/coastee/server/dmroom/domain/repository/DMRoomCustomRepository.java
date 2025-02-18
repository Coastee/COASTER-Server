package com.coastee.server.dmroom.domain.repository;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DMRoomCustomRepository extends JpaRepository<DirectMessageRoom, Long> {

    @Query("""
            select d from DirectMessageRoom d
            join DirectMessageRoomEntry e1 on d = e1.directMessageRoom and e1.user = :A and e1.status = 'ACTIVE'
            join DirectMessageRoomEntry e2 on d = e2.directMessageRoom and e2.user = :B and e2.status = 'ACTIVE'
            where (select count(e3) from DirectMessageRoomEntry e3 where e3.directMessageRoom = d and e3.status = 'ACTIVE') = 2
            """)
    Optional<DirectMessageRoom> findByUserAndUser(
            @Param("A") final User userA,
            @Param("B") final User userB
    );
}
