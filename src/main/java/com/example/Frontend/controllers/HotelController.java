package com.example.Frontend.controllers;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.Entities.Room;
import com.example.Frontend.Service.HotelService;
import com.example.Frontend.Service.RoomService;
import com.example.Frontend.dtos.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    // ── Page 2: Hotel list ──────────────────────────────────────────────────
    @GetMapping
    public String listHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {

        PageView<Hotel> hotels = hotelService.getHotels(page, search);
        model.addAttribute("hotels", hotels);
        model.addAttribute("search", search);
        return "hotels";
    }

    // ── Page 3: Hotel detail + rooms ────────────────────────────────────────
    @GetMapping("/{id}")
    public String hotelDetail(@PathVariable Integer id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        List<Room> rooms = roomService.getRoomsByHotel(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        return "hotel-detail";
    }

    // ── Create hotel ────────────────────────────────────────────────────────
    @PostMapping("/create")
    public String createHotel(@ModelAttribute Hotel hotel, RedirectAttributes ra) {
        try {
            hotelService.createHotel(hotel);
            ra.addFlashAttribute("successMsg", "Hotel \"" + hotel.getName() + "\" created successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Failed to create hotel: " + e.getMessage());
        }
        return "redirect:/hotels";
    }

    // ── Update hotel ────────────────────────────────────────────────────────
    @PostMapping("/{id}/update")
    public String updateHotel(@PathVariable Integer id,
                              @ModelAttribute Hotel hotel,
                              RedirectAttributes ra) {
        try {
            hotelService.updateHotel(id, hotel);
            ra.addFlashAttribute("successMsg", "Hotel updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Failed to update hotel: " + e.getMessage());
        }
        return "redirect:/hotels";
    }

}
