package com.example.Frontend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.Entities.Review;
import com.example.Frontend.Service.HotelService;
import com.example.Frontend.Service.ReviewService;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotels/{hotelId}/reviews")
    public String getReviewsByHotel(
            @PathVariable Integer hotelId,
            @RequestParam(required = false) String location,
            Model model) {

        System.out.println("Fetching reviews for hotel ID: " + hotelId);
        
        Hotel hotel = hotelService.getHotelById(hotelId);
        
        if (hotel == null) {
            model.addAttribute("errorMessage", "Hotel not found");
            model.addAttribute("reviews", List.of());
            model.addAttribute("hotelId", hotelId);
            model.addAttribute("location", location);
            return "reviews";
        }
        
        List<Review> reviews = reviewService.getReviewsByHotelId(hotelId);
        
        model.addAttribute("hotel", hotel);
        model.addAttribute("location", location);
        model.addAttribute("hotelId", hotelId);
        model.addAttribute("reviews", reviews);
        
        System.out.println("Found " + reviews.size() + " reviews for " + hotel.getName());
        
        return "reviews";
    }
}