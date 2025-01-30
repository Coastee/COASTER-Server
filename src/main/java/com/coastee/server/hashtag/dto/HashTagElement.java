package com.coastee.server.hashtag.dto;

import com.coastee.server.hashtag.domain.HashTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class HashTagElement {
    private Long id;
    private String content;

    public HashTagElement(final HashTag hashTag) {
        this.id = hashTag.getId();
        this.content = hashTag.getContent();
    }
}
