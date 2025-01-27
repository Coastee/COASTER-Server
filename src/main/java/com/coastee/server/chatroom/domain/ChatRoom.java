package com.coastee.server.chatroom.domain;

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

    private ChatRoomType chatRoomType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int maxCount;

    private int currentCount;

    private int remainCount;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomTag> tagList = new ArrayList<>();

    public ChatRoom(
            final Server server,
            final User user,
            final String title,
            final String content,
            final ChatRoomType chatRoomType
    ) {
        this.server = server;
        this.user = user;
        this.title = title;
        this.content = content;
        this.chatRoomType = chatRoomType;
    }

    public void enter() {
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