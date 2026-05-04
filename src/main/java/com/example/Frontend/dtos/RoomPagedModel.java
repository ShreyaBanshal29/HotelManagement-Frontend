package com.example.Frontend.dtos;

import com.example.Frontend.Entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomPagedModel {

    @JsonProperty("content")
    private List<Room> content;
    @JsonProperty("_embedded")
    private RoomEmbedded embedded;

    
    // public List<Room> getRooms() {
    //     return content != null ? content : List.of();
    // }

    public List<Room> getRooms() {
        if (content != null) {
            return content;
        }
        return embedded != null ? embedded.getRoom() : List.of();
    }

    public void setContent(List<Room> content) {
        this.content = content;
    }

    public RoomEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(RoomEmbedded embedded) {
        this.embedded = embedded;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoomEmbedded {
        private List<Room> room;

        public List<Room> getRoom() {
            return room != null ? room : List.of();
        }

        public void setRoom(List<Room> room) {
            this.room = room;
        }
    }
}