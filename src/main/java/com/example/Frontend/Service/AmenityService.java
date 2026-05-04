package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.Entities.Amenity;
import com.example.Frontend.dtos.AmenityResponse;
@Service
public class AmenityService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public AmenityResponse getAmenities(int page, String keyword) {
        String url;
        if (keyword != null && !keyword.trim().isEmpty()) {
            url = baseUrl + "/amenities/search/findByNameContainingIgnoreCase"
                    + "?name=" + keyword
                    + "&page=" + page
                    + "&size=20";
        } else {
            url = baseUrl + "/amenities?page=" + page + "&size=20";
        }
        return restTemplate.getForObject(url, AmenityResponse.class);
    }

    public Amenity getAmenityById(int id) {
        String url = baseUrl + "/amenities/" + id;
        Amenity amenity = restTemplate.getForObject(url, Amenity.class);
        amenity.setAmenityId(id);
        return amenity;
    }

    public void saveAmenity(Amenity amenity) {
        if (amenity.getAmenityId() == null) {
            restTemplate.postForObject(baseUrl + "/amenities", amenity, Amenity.class);
        } else {
            restTemplate.put(baseUrl + "/amenities/" + amenity.getAmenityId(), amenity);
        }
    }
}