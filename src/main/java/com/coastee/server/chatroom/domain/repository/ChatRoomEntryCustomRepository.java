package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.chatroom.domain.repository.dto.FindHasEntered;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomEntryCustomRepository extends JpaRepository<ChatRoomEntry, Long> {

    @Query("""
            select new com.coastee.server.chatroom.domain.repository.dto.FindHasEntered(
                c.id,
                (e.id is not null)
            ) from ChatRoom c
            left join ChatRoomEntry e
                on c.id in :chatRoomList
                and e.chatRoom = c
                and e.user = :user
                and e.status = 'ACTIVE'
            where c.id in :chatRoomList
            """)
    List<FindHasEntered> findHasEnteredByChatRoomList(
            @Param("user") final User user,
            @Param("chatRoomList") final List<Long> chatRoomIdList
    );
}