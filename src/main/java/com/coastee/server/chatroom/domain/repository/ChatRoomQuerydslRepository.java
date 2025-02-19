package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.global.util.QuerydslUtil;
import com.coastee.server.hashtag.domain.HashTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.coastee.server.chatroom.domain.QChatRoom.chatRoom;
import static com.coastee.server.chatroom.domain.QChatRoomTag.chatRoomTag;
import static com.coastee.server.hashtag.domain.QHashTag.hashTag;

@Repository
@RequiredArgsConstructor
public class ChatRoomQuerydslRepository {
    private final JPAQueryFactory query;

    public Page<ChatRoom> findByTypeAndKeywordAndTagList(
            final ChatRoomType chatRoomType,
            final String keyword,
            final List<HashTag> tagList,
            final Pageable pageable
    ) {
        List<ChatRoom> chatRoomList = findByTypeAndKeywordAndTagList(chatRoomType, keyword, tagList)
                .orderBy(QuerydslUtil.getSort(pageable, chatRoom))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.in(findByTypeAndKeywordAndTagList(chatRoomType, keyword, tagList)));

        return PageableExecutionUtils.getPage(chatRoomList, pageable, countQuery::fetchOne);
    }

    private JPQLQuery<ChatRoom> findByTypeAndKeywordAndTagList(
            final ChatRoomType chatRoomType,
            final String keyword,
            final List<HashTag> tagList
    ) {
        return query
                .selectFrom(chatRoom)
                .where(
                        chatRoom.chatRoomType.eq(chatRoomType)
                                .and(likeKeyword(keyword))
                                .and(eqTagList(tagList))
                );
    }

    private BooleanExpression likeKeyword(final String keyword) {
        if (keyword == null) {
            return null;
        }
        return chatRoom.title.like(keyword);
    }

    private BooleanExpression eqTagList(final List<HashTag> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            return null;
        }
        return chatRoom.in(
                query
                        .select(chatRoomTag.chatRoom).from(chatRoomTag)
                        .join(hashTag).on(
                                hashTag.eq(chatRoomTag.hashTag)
                                        .and(hashTag.in(tagList))
                        )
                        .where(chatRoom.eq(chatRoomTag.chatRoom))
                        .groupBy(chatRoomTag.chatRoom)
                        .having(chatRoomTag.chatRoom.count().eq(Long.valueOf(tagList.size())))
        );
    }
}
