package com.example.Frontend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Frontend.Entities.RoomType;
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
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("roomType", new RoomType());
        return "roomtype-form";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("roomType", service.getRoomTypeById(id));
        return "roomtype-form";
    }

    // SAVE (ADD + UPDATE)
    @PostMapping("/save")
    public String saveRoomType(@ModelAttribute RoomType roomType) {
        service.saveRoomType(roomType);
        return "redirect:/roomtypes";
    }
   
}