package com.coastee.server.hashtag.service;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.ChatRoomTag;
import com.coastee.server.chatroom.domain.repository.ChatRoomTagRepository;
import com.coastee.server.hashtag.domain.HashTag;
import com.coastee.server.hashtag.domain.repository.HashTagRepository;
import com.coastee.server.server.domain.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final ChatRoomTagRepository chatRoomTagRepository;

    @Transactional
    public void tag(final ChatRoom chatRoom, final Set<String> tagNameSet) {
        List<HashTag> hashTagList = findOrCreateTag(tagNameSet);
        connectTag(chatRoom, hashTagList);
    }

    public List<HashTag> findPopularTagByServer(final Server server, final Pageable pageable) {
        return hashTagRepository.findPopularTagByServer(server, pageable).getContent();
    }

    private void connectTag(final ChatRoom chatRoom, final List<HashTag> hashTagList) {
        List<ChatRoomTag> chatRoomTagList = hashTagList.stream()
                .map(tag -> new ChatRoomTag(chatRoom, tag))
                .toList();
        chatRoomTagRepository.saveAll(chatRoomTagList);
    }

    private List<HashTag> findOrCreateTag(final Set<String> tagNameSet) {
        List<HashTag> existingTagList = hashTagRepository.findAllByContentIn(tagNameSet);
        Set<String> existingTagNameList = existingTagList.stream()
                .map(HashTag::getContent)
                .collect(Collectors.toSet());
        List<HashTag> newTags = tagNameSet.stream()
                .filter(name -> !existingTagNameList.contains(name))
                .map(HashTag::new)
                .toList();
        if (!newTags.isEmpty()) {
            newTags = hashTagRepository.saveAll(newTags);
        }
        return Stream.concat(existingTagList.stream(), newTags.stream()).toList();
    }
}
