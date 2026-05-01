package com.example.Frontend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Frontend.Service.RoomTypeService;
import com.example.Frontend.dtos.RoomTypeResponse;

@Controller
@RequestMapping("/roomtypes")
public class RoomTypeController {

    @Autowired
    private RoomTypeService service;

    @GetMapping
    public String getRoomTypes(@RequestParam(defaultValue = "0") int page, Model model) {

        RoomTypeResponse response = service.getRoomTypes(page);

        model.addAttribute("roomTypes", response.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", response.getPage().getTotalPages());

        return "roomtypes";
    }
}