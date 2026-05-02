package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.dtos.HotelResponse;

import java.net.URI;
import java.util.Map;

@Service
public class HotelService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8081/api/hotels";

    public HotelResponse getAllHotels(int page) {
        URI uri = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .queryParam("page", page)
                .queryParam("size", 10)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, HotelResponse.class);
    }

    public HotelResponse searchByLocation(String location, int page) {
        URI uri = UriComponentsBuilder
                .fromUriString(BASE_URL + "/search/findByLocation")
                .queryParam("location", location.trim())
                .queryParam("page", page)
                .queryParam("size", 10)
                .build()
                .encode()
                .toUri();

        try {
            return restTemplate.getForObject(uri, HotelResponse.class);
        } catch (Exception e) {
            System.err.println("Error searching by location: " + e.getMessage());
            return null;
        }
    }

    public HotelResponse searchByName(String name, int page) {
        URI uri = UriComponentsBuilder
                .fromUriString(BASE_URL + "/search/findByName")
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", 10)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, HotelResponse.class);
    }

    // ✅ YOUR ROBUST METHOD HERE
    public Hotel getHotelById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Hotel ID cannot be null");
        }

        String url = BASE_URL + "/" + id;
        System.out.println("GET HOTEL BY ID URL: " + url);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            return null;
        }

        Hotel hotel = new Hotel();

        // ✅ map fields safely
        hotel.setName((String) response.get("name"));
        hotel.setLocation((String) response.get("location"));
        hotel.setDescription((String) response.get("description"));

        // 🔥 IMPORTANT: handle BOTH cases
        try {
            Object linksObj = response.get("_links");

            if (linksObj != null) {
                Map<String, Object> links = (Map<String, Object>) linksObj;
                Map<String, Object> self = (Map<String, Object>) links.get("self");

                if (self != null && self.get("href") != null) {
                    String href = (String) self.get("href");

                    Integer extractedId = Integer.parseInt(
                            href.substring(href.lastIndexOf("/") + 1)
                    );

                    hotel.setHotelId(extractedId);
                    System.out.println("Extracted ID from _links: " + extractedId);
                    return hotel;
                }
            }

            // ✅ FALLBACK (VERY IMPORTANT)
            // if no _links, use ID from URL
            hotel.setHotelId(id);
            System.out.println("Using fallback ID: " + id);

        } catch (Exception e) {
            // fallback instead of crash
            hotel.setHotelId(id);
            System.out.println("Exception caught, using fallback ID: " + id);
        }

        return hotel;
    }

    public void updateHotel(Integer id, Hotel hotel) {
        String url = BASE_URL + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Create a map for the request body
        Map<String, String> requestBody = Map.of(
            "name", hotel.getName(),
            "location", hotel.getLocation(),
            "description", hotel.getDescription()
        );
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        System.out.println("UPDATED HOTEL ID: " + id);
    }

    public void addHotel(Hotel hotel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = Map.of(
            "name", hotel.getName(),
            "location", hotel.getLocation(),
            "description", hotel.getDescription()
        );
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.postForObject(BASE_URL, entity, Void.class);
        System.out.println("CREATED HOTEL");
    }
}