package com.example.Frontend.Controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.Service.HotelService;
import com.example.Frontend.dtos.HotelResponse;

@Controller
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService service;

    private static final String DEFAULT_LOCATION = "Mumbai";

    @GetMapping
    public String getHotels(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String location,
                            Model model) {

        String activeLocation = (location != null && !location.trim().isEmpty())
                ? location.trim() : DEFAULT_LOCATION;

        HotelResponse response = service.searchByLocation(activeLocation, page);

        List<Hotel> hotels = Collections.emptyList();
        int totalPages = 0;

        if (response != null && response.getContent() != null && !response.getContent().isEmpty()) {
            hotels = response.getContent();
            if (response.getPage() != null) {
                totalPages = response.getPage().getTotalPages();
            }
        }

        model.addAttribute("hotels", hotels);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("location", activeLocation);

        return "hotels";
    }

    @GetMapping("/add")
    public String showAddForm(@RequestParam(required = false) String location, Model model) {
        model.addAttribute("location", location != null ? location : DEFAULT_LOCATION);
        return "add-hotel";
    }

    @PostMapping("/add")
    public String addHotel(@RequestParam String name,
                          @RequestParam String location,
                          @RequestParam String description,
                          @RequestParam(required = false) String locationParam) {
        
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setLocation(location);
        hotel.setDescription(description);
        
        service.addHotel(hotel);
        
        String loc = (locationParam != null && !locationParam.trim().isEmpty()) 
                ? locationParam : DEFAULT_LOCATION;
        
        return "redirect:/hotels?location=" + loc;
    }

    @GetMapping("/edit/{id}")
    public String editHotelForm(@PathVariable Integer id,
                                @RequestParam(required = false) String location,
                                Model model) {
        
        System.out.println("EDIT HOTEL ID: " + id);
        Hotel hotel = service.getHotelById(id);
        
        if (hotel == null || hotel.getHotelId() == null) {
            throw new RuntimeException("Hotel not found for ID: " + id);
        }
        
        System.out.println("Found hotel: ID=" + hotel.getHotelId() + ", Name=" + hotel.getName());
        
        model.addAttribute("hotel", hotel);
        model.addAttribute("location", location != null ? location : DEFAULT_LOCATION);
        return "edit-hotel";
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable Integer id,
                             @RequestParam String name,
                             @RequestParam String location,
                             @RequestParam String description,
                             @RequestParam(required = false) String locationParam) {
        
        System.out.println("UPDATING HOTEL ID: " + id);
        
        Hotel hotel = new Hotel();
        hotel.setHotelId(id);
        hotel.setName(name);
        hotel.setLocation(location);
        hotel.setDescription(description);
        
        service.updateHotel(id, hotel);
        
        String loc = (locationParam != null && !locationParam.trim().isEmpty()) 
                ? locationParam : DEFAULT_LOCATION;
        
        return "redirect:/hotels?location=" + loc;
    }
}