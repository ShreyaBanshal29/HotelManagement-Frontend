package com.example.Frontend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.Frontend.Entities.RoomType;
import com.example.Frontend.dtos.RoomTypeResponse;

@Service
public class RoomTypeService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${backend.base-url}")
   String baseUrl;

    public RoomTypeResponse searchAll(String name, Double price, Integer occupancy, int page) {

        String BASE_URL = baseUrl +"/roomtypes";
    	String url = BASE_URL + "/search/searchAll?" +
    	        (name != null ? "name=" + name + "&" : "") +
    	        (price != null ? "price=" + price + "&" : "") +
    	        (occupancy != null ? "occupancy=" + occupancy + "&" : "") +
    	        "page=" + page+"&size=12";

        return restTemplate.getForObject(url, RoomTypeResponse.class);
    }
    
    public RoomType getRoomTypeById(int id) {

        String BASE_URL = baseUrl +"/roomtypes";
        String url = BASE_URL + "/" + id;
        RoomType room = restTemplate.getForObject(url, RoomType.class);
        room.setRoomTypeId(id);
        return room;
    }
    
    public void saveRoomType(RoomType roomType) {

        String BASE_URL = baseUrl +"/roomtypes";
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