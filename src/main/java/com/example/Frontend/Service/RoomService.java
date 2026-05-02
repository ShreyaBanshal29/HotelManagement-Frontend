package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.Frontend.dtos.RoomResponse;

@Service
public class RoomService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8085/api/room/search/findByRoomType_RoomTypeId?roomTypeId=";

    public RoomResponse getRoomsByRoomType(int page,int id) {

        String url = BASE_URL + id + "&page="+page+"&size=10";
        RoomResponse response =
                restTemplate.getForObject(url, RoomResponse.class);
        return response;
    }
}