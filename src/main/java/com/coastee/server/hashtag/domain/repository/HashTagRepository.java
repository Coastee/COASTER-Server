package com.coastee.server.hashtag.domain.repository;

import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.server.domain.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    List<HashTag> findAllByContentIn(final Set<String> contentSet);

    @Query("""
            select h from ChatRoomTag t
                join HashTag h on t.hashTag = h
                join ChatRoom r on t.chatRoom = r and r.server = :server
            group by h
            order by count (h) desc
            """)
    Page<HashTag> findPopularTagByServer(
            @Param("server") final Server server,
            final Pageable pageable
    );
}
