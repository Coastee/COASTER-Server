package com.coastee.server.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;

public class QuerydslUtil {
    public static <T> OrderSpecifier<?>[] getSort(
            final Pageable pageable,
            final EntityPathBase<T> qClass
    ) {
        return pageable.getSort().stream()
                .map(order -> new OrderSpecifier(
                        Order.valueOf(order.getDirection().name()),
                        Expressions.path(Object.class, qClass, order.getProperty())
                ))
                .toList()
                .toArray(new OrderSpecifier[0]);
    }
}
