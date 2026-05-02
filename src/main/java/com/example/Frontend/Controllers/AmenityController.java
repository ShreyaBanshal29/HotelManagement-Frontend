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

import com.example.Frontend.Entities.Amenity;
import com.example.Frontend.Service.AmenityService;
import com.example.Frontend.dtos.AmenityResponse;

@Controller
@RequestMapping("/amenities")
public class AmenityController {
	
	@Autowired
    private AmenityService service;

    @GetMapping
    public String getAmenities(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(required = false, defaultValue = "") String keyword,
    		Model model) {

        AmenityResponse response = service.getAmenities(page, keyword);

        model.addAttribute("amenities", response.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", response.getPage().getTotalPages());
        model.addAttribute("keyword", keyword);

        return "amenities";
    }
    
    // ADD NEW AMENITY
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("amenity", new Amenity());
        return "amenity-form";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("amenity", service.getAmenityById(id));
        return "amenity-form";
    }

    // SAVE (ADD + UPDATE)
    @PostMapping("/save")
    public String saveAmenity(@ModelAttribute Amenity amenity) {
        service.saveAmenity(amenity);
        return "redirect:/amenities";
    }
	    
}
