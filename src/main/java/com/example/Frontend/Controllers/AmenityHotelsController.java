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

import com.example.Frontend.Entities.HotelAmenity;
import com.example.Frontend.Service.AmenityHotelsService;
import com.example.Frontend.dtos.HotelAmenityResponse;

@Controller
@RequestMapping("/amenities")
public class AmenityHotelsController {

    @Autowired
    private AmenityHotelsService service;

    @GetMapping("/{id}/hotels")
    public String getHotelsByAmenity(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {

        HotelAmenityResponse response =
                service.getHotelsByAmenity(page, id);

        List<HotelAmenity> hotelAmenities =
                (response != null && response.getContent() != null)
                ? response.getContent()
                : Collections.emptyList();

        model.addAttribute("hotelAmenities", hotelAmenities);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", response.getPage().getTotalPages());
        model.addAttribute("amenityId", id);

        return "amenity-hotels";
    }
}