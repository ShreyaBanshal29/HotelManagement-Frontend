package com.example.Frontend.dtos;

import java.util.List;
import com.example.Frontend.Entities.Hotel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelEmbedded {

    private List<Hotel> hotels;

    public List<Hotel> getHotels() { return hotels; }
    public void setHotels(List<Hotel> hotels) { this.hotels = hotels; }
}