package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.Frontend.Entities.RoomType;
import com.example.Frontend.dtos.RoomTypeResponse;

@Service
public class RoomTypeService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8081/api/roomtypes";

    public RoomTypeResponse getRoomTypes(int page) {
    	String url = BASE_URL + "?page=" + page + "&size=20";
        RoomTypeResponse response =
            restTemplate.getForObject(url, RoomTypeResponse.class);


        return response;
    }
    public RoomType getRoomTypeById(int id) {
        String url = BASE_URL + "/" + id;
        RoomType room = restTemplate.getForObject(url, RoomType.class);
        room.setRoomTypeId(id);
        return room;
    }
    
    public void saveRoomType(RoomType roomType) {
        if (roomType.getRoomTypeId() == null) {
            // CREATE
            restTemplate.postForObject(BASE_URL, roomType, RoomType.class);
        } else {
            // UPDATE	
            String url = BASE_URL + "/" + roomType.getRoomTypeId();
            restTemplate.put(url, roomType);
        }
    }
}	