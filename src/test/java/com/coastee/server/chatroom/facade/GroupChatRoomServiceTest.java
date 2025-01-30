package com.coastee.server.chatroom.facade;

import com.coastee.server.auth.domain.Accessor;
import com.coastee.server.chatroom.domain.Scope;
import com.coastee.server.chatroom.dto.ChatRoomElement;
import com.coastee.server.chatroom.dto.ChatRoomElements;
import com.coastee.server.chatroom.dto.request.CreateGroupChatRequest;
import com.coastee.server.fixture.ServerFixture;
import com.coastee.server.fixture.UserFixture;
import com.coastee.server.server.domain.Server;
import com.coastee.server.server.domain.repository.ServerRepository;
import com.coastee.server.user.domain.User;
import com.coastee.server.util.ServiceTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("그룹챗 Facade 테스트")
class GroupChatRoomServiceTest extends ServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private GroupChatRoomFacade groupChatRoomFacade;

    @Autowired
    protected ServerRepository serverRepository;

    private Server server;

    @DisplayName("전체 그룹챗 조회")
    @Test
    void findAllByServer() throws Exception {
        // given
        server = serverRepository.save(ServerFixture.get());
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title A", "content A", Set.of("#A", "#B", "C")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title B", "content B", Set.of("#D", "#E", "F")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title C", "content C", Set.of("#G", "#H", "I")),
                null
        );

        // when
        ChatRoomElements elements = groupChatRoomFacade.findByScope(
                Accessor.user(currentUser.getId()),
                server.getId(),
                Scope.all,
                PageRequest.of(0, 40)
        );

        // then
        assertAll(
                () -> assertEquals(3, elements.getChatRoomList().size()),
                () -> assertThat(elements.getChatRoomList())
                        .extracting(ChatRoomElement::getTitle)
                        .containsExactly("title C", "title B", "title A")
        );
    }

    @DisplayName("전체 그룹챗 조회_이름 기준 정렬")
    @Test
    void findAllByServer_WhenSortByName() throws Exception {
        // given
        server = serverRepository.save(ServerFixture.get());
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title A", "content A", Set.of("#A", "#B", "C")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title B", "content B", Set.of("#D", "#E", "F")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title C", "content C", Set.of("#G", "#H", "I")),
                null
        );

        // when
        ChatRoomElements elements = groupChatRoomFacade.findByScope(
                Accessor.user(currentUser.getId()),
                server.getId(),
                Scope.all,
                PageRequest.of(0, 40, Sort.by(Sort.Direction.ASC, "name"))
        );

        // then
        assertAll(
                () -> assertEquals(3, elements.getChatRoomList().size()),
                () -> assertThat(elements.getChatRoomList())
                        .extracting(ChatRoomElement::getTitle)
                        .containsExactly("title A", "title B", "title C")
        );
    }

    @DisplayName("개설한 그룹챗 조회")
    @Test
    void findAllByServerAndOwner() throws Exception {
        // given
        User anotherUser = userRepository.save(UserFixture.get("anotherUser"));
        server = serverRepository.save(ServerFixture.get());
        groupChatRoomFacade.create(
                Accessor.user(anotherUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title A", "content A", Set.of("#A", "#B", "C")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title B", "content B", Set.of("#D", "#E", "F")),
                null
        );
        groupChatRoomFacade.create(
                Accessor.user(currentUser.getId()),
                server.getId(),
                new CreateGroupChatRequest("title C", "content C", Set.of("#G", "#H", "I")),
                null
        );

        // when
        ChatRoomElements elements = groupChatRoomFacade.findByScope(
                Accessor.user(currentUser.getId()),
                server.getId(),
                Scope.owner,
                PageRequest.of(0, 40)
        );

        // then
        assertAll(
                () -> assertEquals(2, elements.getChatRoomList().size()),
                () -> assertThat(elements.getChatRoomList())
                        .extracting(ChatRoomElement::getTitle)
                        .containsExactly("title C", "title B")
        );
    }

    @DisplayName("참여한 그룹챗 조회")
    @Test
    void findAllByServerAndParticipant() throws Exception {
        // given

        // when

        // then
    }
}