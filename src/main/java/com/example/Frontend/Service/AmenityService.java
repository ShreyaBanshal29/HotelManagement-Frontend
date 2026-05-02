package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.Entities.Amenity;
import com.example.Frontend.dtos.AmenityResponse;

@Service
public class AmenityService {

	@Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8085/api/amenities";

    public AmenityResponse getAmenities(int page, String keyword) {
        String url;
        if (keyword != null && !keyword.trim().isEmpty()) {
            url = BASE_URL + "/search/findByNameContainingIgnoreCase"
                    + "?name=" + keyword
                    + "&page=" + page
                    + "&size=20";
        } else {
            url = BASE_URL + "?page=" + page + "&size=20";
        }
        return restTemplate.getForObject(url, AmenityResponse.class);
    }
    
    public Amenity getAmenityById(int id) {
        String url = BASE_URL + "/" + id;
        Amenity amenity = restTemplate.getForObject(url, Amenity.class);
        amenity.setAmenityId(id);
        return amenity;
    }
    
    public void saveAmenity(Amenity amenity) {
        if (amenity.getAmenityId() == null) {
            // CREATE
            restTemplate.postForObject(BASE_URL, amenity, Amenity.class);
        } else {
            // UPDATE	
            String url = BASE_URL + "/" + amenity.getAmenityId();
            restTemplate.put(url, amenity);
        }
    }
}
