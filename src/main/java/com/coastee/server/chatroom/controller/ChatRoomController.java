package com.coastee.server.chatroom.controller;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("")
    public ChatRoom createRoom(@RequestParam String title) {
        return chatRoomService.createRoom(title);
    }

    @GetMapping("")
    public List<ChatRoom> getAllRooms() {
        return chatRoomService.findAll();
    }
}
