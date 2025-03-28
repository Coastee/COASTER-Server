package com.coastee.server.chatroom.domain.repository;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomType;
import com.coastee.server.global.util.QuerydslUtil;
import com.coastee.server.server.domain.Server;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.coastee.server.chatroom.domain.QChatRoom.chatRoom;
import static com.coastee.server.chatroom.domain.QChatRoomTag.chatRoomTag;
import static com.coastee.server.hashtag.domain.QHashTag.hashTag;

@Repository
@RequiredArgsConstructor
public class ChatRoomQuerydslRepository {
    private final JPAQueryFactory query;

    public Page<ChatRoom> findByServerAndTypeAndKeywordAndTagList(
            final Server server,
            final ChatRoomType chatRoomType,
            final String keyword,
            final List<String> tagNameList,
            final Pageable pageable
    ) {
        List<ChatRoom> chatRoomList = findByServerAndTypeAndKeywordAndTagList(server, chatRoomType, keyword, tagNameList)
                .orderBy(QuerydslUtil.getSort(pageable, chatRoom))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.in(findByServerAndTypeAndKeywordAndTagList(server, chatRoomType, keyword, tagNameList)));

        return PageableExecutionUtils.getPage(chatRoomList, pageable, countQuery::fetchOne);
    }

    private JPQLQuery<ChatRoom> findByServerAndTypeAndKeywordAndTagList(
            final Server server,
            final ChatRoomType chatRoomType,
            final String keyword,
            final List<String> tagNameList
    ) {
        return query
                .selectFrom(chatRoom)
                .join(chatRoom.user)
                .where(
                        chatRoom.server.eq(server)
                                .and(chatRoom.chatRoomType.eq(chatRoomType))
                                .and(exceptPastMeeting(chatRoomType))
                                .and(containsKeyword(keyword))
                                .and(eqTagList(tagNameList))
                );
    }

    private BooleanExpression exceptPastMeeting(final ChatRoomType chatRoomType) {
        if (chatRoomType != ChatRoomType.MEETING) {
            return null;
        }
        return chatRoom.period.startDate.after(LocalDateTime.now());
    }

    private BooleanExpression containsKeyword(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return chatRoom.title.contains(keyword)
                .or(chatRoom.content.contains(keyword))
                .or(chatRoom.user.nickname.contains(keyword))
                .or(containsTagList(List.of(keyword)))
                ;
    }

    private BooleanExpression eqTagList(
            final List<String> tagNameList
    ) {
        if (tagNameList == null || tagNameList.isEmpty()) {
            return null;
        }

        BooleanExpression condition = tagNameList.stream()
                .map(hashTag.content::eq)
                .reduce(BooleanExpression::or)
                .orElse(null);

        return chatRoom.in(
                query
                        .select(chatRoomTag.chatRoom).from(chatRoomTag)
                        .join(hashTag).on(condition.and(hashTag.eq(chatRoomTag.hashTag)))
                        .where(chatRoom.eq(chatRoomTag.chatRoom))
                        .groupBy(chatRoomTag.chatRoom)
                        .having(chatRoomTag.chatRoom.count().eq(Long.valueOf(tagNameList.size())))
        );
    }

    private BooleanExpression containsTagList(
            final List<String> tagNameList
    ) {
        if (tagNameList == null || tagNameList.isEmpty()) {
            return null;
        }

        BooleanExpression condition = tagNameList.stream()
                .map(hashTag.content::contains)
                .reduce(BooleanExpression::or)
                .orElse(null);

        return chatRoom.in(
                query
                        .select(chatRoomTag.chatRoom).from(chatRoomTag)
                        .join(hashTag).on(condition.and(hashTag.eq(chatRoomTag.hashTag)))
                        .where(chatRoom.eq(chatRoomTag.chatRoom))
                        .groupBy(chatRoomTag.chatRoom)
        );
    }
}
