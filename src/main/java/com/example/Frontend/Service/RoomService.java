package com.example.Frontend.Service;

import com.example.Frontend.Entities.Room;
import com.example.Frontend.dtos.RoomPagedModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.url}")
    private String baseUrl;

    /**
     * Calls GET /api/room/search/findByHotel_HotelId?hotelId={id}
     * Returns all rooms belonging to that hotel with roomType details inline.
     */
    public List<Room> getRoomsByHotel(Integer hotelId) {
        String url = baseUrl + "/room/search/findByHotel_HotelId?hotelId=" + hotelId
                + "&size=100";
        RoomPagedModel model = restTemplate.getForObject(url, RoomPagedModel.class);
        if (model == null) return List.of();
        List<Room> rooms = model.getRooms();
        return rooms != null ? rooms : List.of();
    }
}
