package com.coastee.server.chatroom.controller;

import com.coastee.server.chatroom.domain.ChatRoom;
import com.coastee.server.chatroom.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/chat/room")
    public String rooms(final Model model) {
        return "chat/room";
    }

    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAll();
    }

    @PostMapping("/chat/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("name") String name) {
        return chatRoomRepository.create(name);
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable Long roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable("roomId") Long roomId) {
        return chatRoomRepository.findById(roomId);
    }
}
