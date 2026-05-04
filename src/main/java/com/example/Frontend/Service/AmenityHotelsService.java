package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.dtos.HotelAmenityResponse;

@Service
public class AmenityHotelsService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL =
        "http://localhost:8085/api/hotelamenities/search/findByAmenityAmenityId?amenityId=";

    public HotelAmenityResponse getHotelsByAmenity(int page, int amenityId) {

        String url = BASE_URL + amenityId + "&page=" + page + "&size=10";
        HotelAmenityResponse response =
                restTemplate.getForObject(url, HotelAmenityResponse.class);

        return response;
    }
}