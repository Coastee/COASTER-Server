package com.coastee.server.dm.dto;

import com.coastee.server.dm.domain.DMType;
import com.coastee.server.dm.domain.DirectMessage;
import com.coastee.server.user.dto.UserElement;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMElement {
    private Long id;
    private UserElement user;
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdDate;

    private DMType type;
    private Long dmRoomId;

    public DMElement(final DirectMessage directMessage) {
        this.id = directMessage.getId();
        this.user = new UserElement(directMessage.getUser());
        this.content = directMessage.getContent();
        this.createdDate = directMessage.getCreatedDate();
        this.type = directMessage.getType();
        this.dmRoomId = directMessage.getDirectMessageRoom().getId();
    }
}
