package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.Frontend.dtos.RoomResponse;

@Service
public class RoomService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${backend.base-url}")
  String baseUrl;


    public RoomResponse getRoomsByRoomType(int page,int id) {
    	String BASE_URL = baseUrl+"/room/search/findByRoomType_RoomTypeId?roomTypeId=";

        String url = BASE_URL + id + "&page="+page+"&size=10";
        RoomResponse response =
                restTemplate.getForObject(url, RoomResponse.class);
        return response;
    }
}