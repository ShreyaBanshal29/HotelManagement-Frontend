package com.example.Frontend.Controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.Entities.HotelAmenity;
import com.example.Frontend.Entities.Room;
import com.example.Frontend.Service.HotelService;
import com.example.Frontend.Service.RoomTypeService;
import com.example.Frontend.dtos.HotelAmenityResponse;
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
                            @RequestParam(defaultValue = "reviews") String type,
                            Model model) {

        String activeLocation = (location != null && !location.trim().isEmpty())
                ? location.trim()
                : DEFAULT_LOCATION;

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
        model.addAttribute("type", type); // 🔥 KEY
        
        return "hotels-chirag";
    }

    @GetMapping("/add")
    public String showAddForm(@RequestParam(required = false) String location, Model model) {
        model.addAttribute("location", location != null ? location : DEFAULT_LOCATION);
        return "add-hotel";
    }
    @GetMapping("/{id}/rooms")
    public String hotelRooms(@PathVariable Integer id,
                             @RequestParam(required = false) String location,
                             Model model) {

        Hotel hotel = service.getHotelById(id);
        List<Room> rooms = service.getRoomsByHotel(id);

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("location", location);

        return "hotel-detail"; // 🔥 your HTML
    }
    @GetMapping("/{id}/amenities")
    public String getAmenitiesByHotel(@PathVariable int id,
    		@RequestParam(required = false) String location,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {

        HotelAmenityResponse response =
                service.getAmenitiesByHotel(page, id);

        List<HotelAmenity> hotelAmenities =
                (response != null && response.getContent() != null)
                ? response.getContent()
                : Collections.emptyList();
        Hotel hotel = (!hotelAmenities.isEmpty())
                ? hotelAmenities.get(0).getHotel()
                : null;

        model.addAttribute("hotel", hotel);
        model.addAttribute("hotelAmenities", hotelAmenities);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", response.getPage().getTotalPages());
        model.addAttribute("hotelId", id);
        model.addAttribute("location", location);

        return "hotel-amenities";
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