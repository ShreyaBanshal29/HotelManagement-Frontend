package com.example.Frontend.Service;

import com.example.Frontend.Entities.Hotel;
import com.example.Frontend.dtos.HotelPagedModel;
import com.example.Frontend.dtos.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.url}")
    private String baseUrl;

    private static final int PAGE_SIZE = 5;

    /** Fetch paginated hotels, optionally filtered by name */
    public PageView<Hotel> getHotels(int page, String search) {
        String url;
        if (search != null && !search.isBlank()) {
            // Use Spring Data REST search endpoint: findByName
            url = baseUrl + "/hotels/search/findByName?name=" + encode(search)
                    + "&page=" + page + "&size=" + PAGE_SIZE;
        } else {
            url = baseUrl + "/hotels?page=" + page + "&size=" + PAGE_SIZE;
        }

        HotelPagedModel model = restTemplate.getForObject(url, HotelPagedModel.class);

        if (model == null || model.getPage() == null) {
            return new PageView<>(List.of(), emptyPage());
        }
        return new PageView<>(model.getHotels(), model.getPage());
    }

    /** Fetch a single hotel by ID */
    public Hotel getHotelById(Integer id) {
        String url = baseUrl + "/hotels/" + id + "?projection=hotelSummary";
        return restTemplate.getForObject(url, Hotel.class);
    }

    /** Create a new hotel via POST */
    public void createHotel(Hotel hotel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Hotel> request = new HttpEntity<>(hotel, headers);
        restTemplate.postForObject(baseUrl + "/hotels", request, String.class);
    }

    /** Update an existing hotel via PATCH */
    public void updateHotel(Integer id, Hotel hotel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Hotel> request = new HttpEntity<>(hotel, headers);
        restTemplate.exchange(baseUrl + "/hotels/" + id, HttpMethod.PATCH, request, String.class);
    }

    private com.example.Frontend.dtos.PageInfo emptyPage() {
        com.example.Frontend.dtos.PageInfo p = new com.example.Frontend.dtos.PageInfo();
        p.setNumber(0); p.setSize(PAGE_SIZE); p.setTotalElements(0); p.setTotalPages(0);
        return p;
    }

    private String encode(String s) {
        try { return java.net.URLEncoder.encode(s, "UTF-8"); }
        catch (Exception e) { return s; }
    }
}
