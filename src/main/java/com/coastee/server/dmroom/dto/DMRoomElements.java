package com.coastee.server.dmroom.dto;

import com.coastee.server.dmroom.domain.DirectMessageRoom;
import com.coastee.server.dmroom.domain.repository.dto.FindAnotherUserAndRecentDM;
import com.coastee.server.global.domain.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRoomElements {
    private PageInfo pageInfo;
    private List<DMRoomElement> dmRoomList;

    public DMRoomElements(
            final Page<DirectMessageRoom> dmRoomPage,
            final Map<Long, FindAnotherUserAndRecentDM> dmRoomMap
    ) {
        this.pageInfo = new PageInfo(dmRoomPage);
        dmRoomList = dmRoomPage.getContent().stream().map(
                r -> {
                    FindAnotherUserAndRecentDM dto = dmRoomMap.get(r.getId());
                    return new DMRoomElement(r, dto.getUser(), dto.getDm());
                }
        ).toList();
    }
}
