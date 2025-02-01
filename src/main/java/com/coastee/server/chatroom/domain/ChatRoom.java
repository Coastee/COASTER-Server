package com.coastee.server.chatroom.domain;

import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.server.domain.Server;
import com.coastee.server.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus.MAX_PARTICIPANT;
import static com.coastee.server.global.domain.Constant.MAX_COUNT;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String thumbnail;

    private String title;

    private String content;

    @Enumerated(STRING)
    private ChatRoomType chatRoomType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int maxCount;

    private int currentCount;

    private int remainCount;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomTag> tagList = new ArrayList<>();

    private ChatRoom(
            final Server server,
            final User user,
            final String title,
            final String content,
            final ChatRoomType chatRoomType,
            final LocalDateTime startDate,
            final LocalDateTime endDate,
            final int maxCount
    ) {
        this.server = server;
        this.user = user;
        this.title = title;
        this.content = content;
        this.chatRoomType = chatRoomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxCount = maxCount;
        this.currentCount = 0;
        this.remainCount = maxCount;
    }

    public static ChatRoom groupChatRoom(
            final Server server,
            final User user,
            final String title,
            final String content
    ) {
        return new ChatRoom(
                server,
                user,
                title,
                content,
                ChatRoomType.GROUP,
                null,
                null,
                MAX_COUNT
        );
    }

    public void enter() {
        if (remainCount <= 0) {
            throw new GeneralException(MAX_PARTICIPANT);
        }
        currentCount += 1;
        remainCount = maxCount - currentCount;
    }

    public void increaseMaxCount(final int count) {
        maxCount += count;
        remainCount = maxCount - currentCount;
    }

    public void updateThumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
    }
}