package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.dtos.HotelAmenityResponse;

@Service
public class HotelAminitiesService {


    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${backend.base-url}")
    private String baseUrl;

    public HotelAmenityResponse getAmenitiesByHotel(int page, int hotelId) {

        String url = baseUrl + "/hotelamenities/search/findByHotelHotelId?hotelId=" + hotelId + "&page=" + page + "&size=10";
        HotelAmenityResponse response =
                restTemplate.getForObject(url, HotelAmenityResponse.class);

        return response;
    }
}