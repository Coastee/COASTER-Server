package com.coastee.server.chatroom.domain;

import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.hashtag.domain.HashTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
public class ChatRoomTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    public ChatRoomTag(final ChatRoom chatRoom, final HashTag hashTag) {
        this.chatRoom = chatRoom;
        chatRoom.getTagList().add(this);
        this.hashTag = hashTag;
    }
}
