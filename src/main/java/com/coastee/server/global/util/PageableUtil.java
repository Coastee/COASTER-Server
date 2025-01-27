package com.coastee.server.global.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    public static Pageable setSortOrder(final Pageable pageable) {
        if (isSortBy(pageable, "name")) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "chat_room.title")
            );
        } else if (isSortBy(pageable, "remain")) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "chat_room.remain_count")
            );
        } else {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "chat_room.created_date")
            );
        }
    }

    private static boolean isSortBy(final Pageable pageable, final String property) {
        return pageable.getSort().getOrderFor(property) != null;
    }
}
