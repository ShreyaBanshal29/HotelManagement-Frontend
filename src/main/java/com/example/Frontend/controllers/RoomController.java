package com.example.Frontend.Controllers;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Frontend.Entities.Room;
import com.example.Frontend.Service.RoomService;
import com.example.Frontend.dtos.RoomResponse;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping("/byRoomId/{id}")
    public String getRoomsByRoomType(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {

        RoomResponse response = service.getRoomsByRoomType(page, id);

        List<Room> rooms = (response != null && response.getRooms() != null)
                ? response.getRooms()
                : Collections.emptyList();

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", response.getPage().getTotalPages());
        model.addAttribute("roomTypeId", id);

        return "rooms";
    }
}