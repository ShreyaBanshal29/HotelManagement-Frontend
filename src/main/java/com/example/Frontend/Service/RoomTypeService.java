package com.example.Frontend.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Frontend.Entities.RoomType;
import com.example.Frontend.dtos.RoomTypeResponse;

@Service
public class RoomTypeService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8085/api/roomtypes";

    public RoomTypeResponse getRoomTypes(int page) {
    	String url = BASE_URL + "?page=" + page + "&size=20";
        RoomTypeResponse response =
            restTemplate.getForObject(url, RoomTypeResponse.class);


        return response;
    }
}	