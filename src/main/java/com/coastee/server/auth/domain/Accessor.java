package com.coastee.server.auth.domain;

import lombok.Getter;

@Getter
public class Accessor {

    private final Long memberId;
    private final Authority authority;

    private Accessor(final Long memberId, final Authority authority) {
        this.memberId = memberId;
        this.authority = authority;
    }

    public static Accessor member(final Long memberId) {
        return new Accessor(memberId, Authority.MEMBER);
    }

    public static Accessor admin(final Long memberId) {
        return new Accessor(memberId, Authority.ADMIN);
    }

    public boolean isMember() {
        return Authority.MEMBER.equals(authority);
    }

    public boolean isAdmin() {
        return Authority.ADMIN.equals(authority);
    }
}
