package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.Entities.Amenity;
import com.example.Frontend.dtos.HotelAmenityResponse;

@Service
public class AmenityHotelsService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${backend.base-url}")
    private String baseUrl;

    public HotelAmenityResponse getHotelsByAmenity(int page, int amenityId) {

        String url = baseUrl + "/hotelamenities/search/findByAmenityAmenityId?amenityId=" + amenityId + "&page=" + page + "&size=10";
        HotelAmenityResponse response =
                restTemplate.getForObject(url, HotelAmenityResponse.class);

        return response;
    }
    
    public Amenity getAmenityById(int id) {
        String url = baseUrl + "/amenities/" + id;
        return restTemplate.getForObject(url, Amenity.class);
    }
}