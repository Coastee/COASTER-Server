package com.coastee.server.global.util;

import com.coastee.server.chatroom.domain.SortType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    public static Pageable setSortOrder(final Pageable pageable) {
        if (isSortBy(pageable, SortType.name.getCode())) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "title")
            );
        } else if (isSortBy(pageable, SortType.remain.getCode())) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "remainCount")
            );
        } else {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdDate")
            );
        }
    }

    private static boolean isSortBy(final Pageable pageable, final String property) {
        return pageable.getSort().getOrderFor(property) != null;
    }
}
