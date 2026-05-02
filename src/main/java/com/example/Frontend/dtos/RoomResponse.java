package com.example.Frontend.dtos;

import java.util.List;
import com.example.Frontend.Entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomResponse {

    // ✅ Match actual JSON
    @JsonProperty("content")
    private List<Room> rooms;

    private PageInfo page;

    public List<Room> getRooms() {
        return rooms != null ? rooms : List.of();
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "RoomResponse{" +
                "rooms=" + rooms +
                ", page=" + page +
                '}';
    }
}