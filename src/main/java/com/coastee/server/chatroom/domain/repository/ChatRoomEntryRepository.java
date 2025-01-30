package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomEntry;
import com.coastee.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {

    @Query("""
            select new map (e.chatRoom.id, (c.id != null)) from ChatRoomEntry e
            left join ChatRoom c
                on e.chatRoom = c
                and e.user = :user
                and e.chatRoom in :chatRoomList
            """)
    HashMap<Long, Boolean> findHasJoinedByChatRoomList(
            @Param("user") final User user,
            @Param("chatRoomList") final List<ChatRoom> chatRoomList
    );
}