package com.example.Frontend.dtos;

import com.example.Frontend.Entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomPagedModel {

    @JsonProperty("content")
    private List<Room> content;

    public List<Room> getRooms() {
        return content != null ? content : List.of();
    }

    public void setContent(List<Room> content) {
        this.content = content;
    }
}