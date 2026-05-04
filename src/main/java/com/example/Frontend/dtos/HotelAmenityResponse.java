package com.example.Frontend.dtos;

import java.util.List;

import com.example.Frontend.Entities.HotelAmenity;

public class HotelAmenityResponse {

    private List<HotelAmenity> content;
    private PageInfo page;

    public List<HotelAmenity> getContent() {
        return content;
    }

    public void setContent(List<HotelAmenity> content) {
        this.content = content;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}