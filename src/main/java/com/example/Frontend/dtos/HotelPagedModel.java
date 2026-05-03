package com.example.Frontend.dtos;

import com.example.Frontend.Entities.Hotel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelPagedModel {

    @JsonProperty("content")
    private List<Hotel> content;

    @JsonProperty("page")
    private PageInfo page;

    public List<Hotel> getHotels() {
        return content != null ? content : new ArrayList<>();
    }

    public PageInfo getPage() {
        return page;
    }

    public void setContent(List<Hotel> content) {
        this.content = content;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}
