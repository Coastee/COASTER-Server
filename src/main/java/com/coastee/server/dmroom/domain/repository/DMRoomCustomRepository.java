package com.coastee.server.dmroom.domain.repository;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.repository.dto.FindAnotherUserAndRecentDM;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DMRoomCustomRepository extends JpaRepository<DirectMessageRoom, Long> {

    @Query("""
            select new com.coastee.server.dmroom.domain.repository.dto.FindAnotherUserAndRecentDM(
                r.id, e1.user, d
            ) from DirectMessageRoom r
            join DirectMessageRoomEntry e1
                on r = e1.directMessageRoom
                and e1.user != :user
                and e1.status = 'ACTIVE'
                and r in :dmRoomList
            join fetch e1.user
            left join DirectMessage d
                on r = d.directMessageRoom
                and d.id = (select d2.id from DirectMessage d2
                            where r = d2.directMessageRoom
                            order by d2.createdDate desc limit 1)
            where (select count(e2) from DirectMessageRoomEntry e2 where e2.directMessageRoom = d and e2.status = 'ACTIVE') = 2
            """)
    List<FindAnotherUserAndRecentDM> findAnotherUserAndDMByDMRoomListAndUser(
            @Param("dmRoomList") final List<DirectMessageRoom> dmRoomList,
            @Param("user") final User user
    );
}