package com.coastee.server.server.domain;

import com.coastee.server.global.BaseEntity;
import com.coastee.server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ServerEntry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @Builder(builderMethodName = "of")
    public ServerEntry(final User user, final Server server) {
        this.user = user;
        this.server = server;
    }
}
