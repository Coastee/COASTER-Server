package com.coastee.server.dm.domain;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.global.apipayload.exception.GeneralException;
import com.coastee.server.global.domain.BaseEntity;
import com.coastee.server.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static com.coastee.server.global.apipayload.code.status.ErrorStatus._INVALID_AUTHORITY;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
public class DirectMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "direct_message_room_id")
    private DirectMessageRoom directMessageRoom;

    private String content;

    @Enumerated(EnumType.STRING)
    private DMType type;

    public DirectMessage(
            final User user,
            final DirectMessageRoom directMessageRoom,
            final String content,
            final DMType type
    ) {
        this.user = user;
        this.directMessageRoom = directMessageRoom;
        this.content = content;
        this.type = type;
    }

    @Override
    public void delete() {
        super.delete();
        this.type = DMType.DELETE;
    }

    public void validateUser(final User user) {
        if (this.user.equals(user)) return;
        throw new GeneralException(_INVALID_AUTHORITY);
    }
}
